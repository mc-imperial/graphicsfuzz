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
import javax.imageio.ImageIO;
import org.apache.commons.io.FilenameUtils;
import org.bytedeco.javacpp.opencv_core.Mat;

public abstract class AbstractImageHandler implements Runnable {

  private BufferedImage img0;
  private BufferedImage img1;
  private Mat mask;
  private File output;

  public AbstractImageHandler(
      BufferedImage img0,
      BufferedImage img1,
      Mat mask,
      File output) {
    this.img0 = img0;
    this.img1 = img1;
    this.mask = mask;
    this.output = output;
  }

  @Override
  //Saves image with name 'img_<imgCount>.png'
  public synchronized void run() {
    try {
      if (isDifferent(img0, img1, mask)) {
        ImageIO.write(img0, FilenameUtils.getExtension(output.getName()),
            FileGetter.getUniqueFile(output));
      }
    } catch (IOException exception) {
      exception.printStackTrace();
    }
  }

  public abstract boolean isDifferent(BufferedImage img0, BufferedImage img1, Mat mask)
        throws IOException;
}
