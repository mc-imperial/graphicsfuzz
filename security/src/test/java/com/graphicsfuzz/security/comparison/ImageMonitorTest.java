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

package com.graphicsfuzz.security.comparison;


import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNot.not;

import com.graphicsfuzz.security.StreamTestUtils;
import com.graphicsfuzz.security.tool.ScreenShot;
import com.graphicsfuzz.security.tool.screenmonitor.ImageDifferentiator;
import java.awt.AWTException;
import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class ImageMonitorTest {

  private File outDir;

  @Rule
  public TemporaryFolder testFolder = new TemporaryFolder();

  @Before
  public void init() throws IOException {
    outDir = testFolder.newFolder();
  }

  //Check that ScreenShot.capture() creates a screenshot
  @Test
  public void screenShotTest() throws IOException, AWTException {
    try {
      ScreenShot.capture(new File(outDir, "screenshot.png"));
    } catch (HeadlessException exception) {
      // If we are running headless, this exception will be thrown in which case we abandon
      // the test without complaining.
      return;
    }

    StreamTestUtils.checkDirContains(outDir.getPath(), 1);
  }

  //Check that imageDifferentiator correctly detects different images using isDifferentExact
  @Test
  public void identicalImagesNotDifferentExact() throws IOException, AWTException {
    BufferedImage img0 = StreamTestUtils.createColorImg(Color.WHITE);
    BufferedImage img1 = StreamTestUtils.createColorImg(Color.WHITE);

    assertFalse(ImageDifferentiator.isDifferentExact(img0, img1, new Mat()));
  }

  //Check that identical images are found to be identical by getDifferenceHist (i.e. returns 0.0)
  @Test
  public void identicalImagesNotDifferentHist() throws IOException, AWTException {
    BufferedImage img0 = StreamTestUtils.createColorImg(Color.WHITE);
    BufferedImage img1 = StreamTestUtils.createColorImg(Color.WHITE);

    assertEquals(0.0, ImageDifferentiator.getDifferenceHist(img0, img1, new Mat()));
  }

  //Check that different images are detected to be different by isDifferentExact
  @Test
  public void diffImagesDifferentExact() throws IOException {
    BufferedImage img0 = StreamTestUtils.createColorImg(Color.WHITE);
    BufferedImage img1 = StreamTestUtils.createColorImg(Color.BLACK);

    assertTrue(ImageDifferentiator.isDifferentExact(img0, img1, new Mat()));
  }

  //Check that different images are found to be different by getDifferenceHist
  @Test
  public void diffImagesDifferentHist() throws IOException {
    BufferedImage img0 = StreamTestUtils.createColorImg(Color.WHITE);
    BufferedImage img1 = StreamTestUtils.createColorImg(Color.BLACK);

    assertThat(ImageDifferentiator.getDifferenceHist(img0, img1, new Mat()), not(0.0));
  }

  //Get timings for certain actions (screen capture, image differentiation, and saving)
  //Utility, never fails but takes time. Remove @Ignore tag to run
  @Ignore("Always passes but takes time so shouldn't be run with other tests")
  @Test
  public void timingTest() throws AWTException, IOException {
    float runningAvgSS = 0;
    float runningAvgDC = 0;
    float runningAvgIS = 0;
    float runningAvgEC = 0;
    float runningAvgDDC = 0;
    float nanoToSeconds = 1f / 1000000000f;
    for (int i = 0; i < 20; i++) {
      BufferedImage bi1 = ScreenShot.capture();
      BufferedImage bi2 = ScreenShot.capture();
      BufferedImage bi3 = ScreenShot.capture();
      long t0 = System.nanoTime();
      BufferedImage bi0 = ScreenShot.capture();
      long t1 = System.nanoTime();
      Mat m0 = ImageDifferentiator.bufferedImageToMat(bi0);
      Mat m1 = ImageDifferentiator.bufferedImageToMat(bi1);
      double d0 = ImageDifferentiator.getDifferenceHist(m0, m1, m1);
      long t2 = System.nanoTime();
      ImageIO.write(bi0, "png", testFolder.newFolder());
      long t3 = System.nanoTime();
      Mat m2 = ImageDifferentiator.bufferedImageToMat(bi2);
      Mat m3 = ImageDifferentiator.bufferedImageToMat(bi3);
      ImageDifferentiator.isDifferentExact(m2, m3, m3);
      long t4 = System.nanoTime();
      Mat m4 = ImageDifferentiator.bufferedImageToMat(bi0);
      Mat m5 = ImageDifferentiator.bufferedImageToMat(bi1);
      double d1 = ImageDifferentiator.getDifferenceHistDirect(m4, m5, m5);
      long t5 = System.nanoTime();
      runningAvgSS = runningAvg(runningAvgSS, i, t1 - t0);
      runningAvgDC = runningAvg(runningAvgDC, i, t2 - t1);
      runningAvgIS = runningAvg(runningAvgIS, i, t3 - t2);
      runningAvgEC = runningAvg(runningAvgEC, i, t4 - t3);
      runningAvgDDC = runningAvg(runningAvgDDC, i, t5 - t4);
      System.out.println(d0);
      System.out.println(d1);

    }
    System.out.println("ScreenShot time: " + runningAvgSS * nanoToSeconds + "s");
    System.out.println("Normal histogram difference calculation time: " + runningAvgDC * nanoToSeconds + "s");
    System.out.println("Image saving time: " + runningAvgIS * nanoToSeconds + "s");
    System.out.println("Exact difference calculation time: " + runningAvgEC * nanoToSeconds + "s");
    System.out.println("Direct histogram difference calculation time: " + runningAvgDDC * nanoToSeconds + "s");
  }

  //Calculate a running average given the previous average, the number of values averaged so far,
  //and the latest value
  private float runningAvg(float avg, int i, float value) {
    return ((avg * i) + value) / (i + 1);
  }


}
