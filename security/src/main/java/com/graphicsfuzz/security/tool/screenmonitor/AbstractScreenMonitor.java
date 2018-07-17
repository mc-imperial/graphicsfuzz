// Copyright (c) 2018 Imperial College London
//
// Permission is hereby granted, free of charge, to any person obtaining
// a copy of this software and associated documentation files (the
// "Software"), to deal in the Software without restriction, including
// without limitation the rights to use, copy, modify, merge, publish,
// distribute, sublicense, and/or sell copies of the Software, and to
// permit persons to whom the Software is furnished to do so, subject to
// the following conditions:
//
// The above copyright notice and this permission notice shall be
// included in all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
// EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
// MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
// NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
// LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
// OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
// WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

package com.graphicsfuzz.security.tool.screenmonitor;

import com.graphicsfuzz.security.tool.FileGetter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.imageio.ImageIO;
import org.bytedeco.javacpp.opencv_core.Mat;

public abstract class AbstractScreenMonitor {

  private File outDir;
  private long lifetime;
  private ScheduledExecutorService executor;
  private ExecutorService ioExecutor;
  private int imgCount = 0;
  private int lastImg = 1;
  private BufferedImage img0 = null;
  private BufferedImage img1 = null;
  private Mat maskMat = new Mat();
  private int time = 0;
  private int timeupdateinterval = 10;


  public AbstractScreenMonitor(File outDir, long lifetime, int ssThreads, int ioThreads,
      int timeUpdateInterval) {
    this.outDir = outDir;
    outDir.mkdirs();
    this.lifetime = lifetime;
    this.timeupdateinterval = timeUpdateInterval;
    executor = Executors.newScheduledThreadPool(ssThreads);
    ioExecutor = Executors.newFixedThreadPool(ioThreads);
  }

  //Begin screen recording after <delay> seconds
  public void start(long delay) throws InterruptedException {
    //Schedule screenshots
    executor.schedule(new ScreenShotTask(this, executor), delay, TimeUnit.SECONDS);
    executor.scheduleAtFixedRate(() -> {
      time += timeupdateinterval;
      System.out.println("rec time: " + time);
    }, timeupdateinterval + delay, timeupdateinterval, TimeUnit.SECONDS);

    //Schedule shutdowns
    executor.schedule(() -> {
      stop();
    }, lifetime + delay, TimeUnit.SECONDS);
  }

  private void stop() {
    System.out.println("Recording Complete - Stopping...");
    executor.shutdown();
    try {
      executor.awaitTermination(30, TimeUnit.SECONDS);
    } catch (InterruptedException exception) {
      exception.printStackTrace();
    }
    ioExecutor.shutdown();
    try {
      ioExecutor.awaitTermination(30, TimeUnit.SECONDS);
    } catch (InterruptedException exception) {
      exception.printStackTrace();
    }
  }

  //Set the mask
  public void calibrate(File mask) throws IOException {
    maskMat = ImageDifferentiator.bufferedImageToMat(ImageIO.read(mask));
  }

  //Check new screenshot for differences against last different screenshot
  public synchronized void update(BufferedImage image) throws IOException {
    //Set image
    if (lastImg == 1) {
      img0 = image;
      lastImg = 1 - lastImg;
    } else {
      img1 = image;
      lastImg = 1 - lastImg;
    }

    //If either image not set, return and flip lastImg
    if (img0 == null || img1 == null) {
      return;
    }

    //If first comparison, print 'Recording' to console
    if (imgCount == 0) {
      System.out.println("Recording");
    }

    //Compare the last different image with the most recent image, save and flip lastImg if
    //difference (by chi square of histograms) > threshold
    AbstractImageHandler imageHandler;
    File output = FileGetter.getUniqueFile(new File(outDir, "img_" + imgCount + ".png"));
    if (lastImg == 0) {
      imageHandler = getImageHandler(img0, img1, maskMat, output);
    } else {
      imageHandler = getImageHandler(img1, img0, maskMat, output);
    }
    ioExecutor.submit(imageHandler);
    imgCount++;
  }

  abstract AbstractImageHandler getImageHandler(BufferedImage img0, BufferedImage img1, Mat mask,
      File output);

}
