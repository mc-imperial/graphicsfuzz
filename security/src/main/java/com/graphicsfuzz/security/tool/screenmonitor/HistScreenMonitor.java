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

import java.awt.image.BufferedImage;
import java.io.File;
import org.bytedeco.javacpp.opencv_core.Mat;

//Periodically takes screenshots and saves if they are significantly different from previous
public class HistScreenMonitor extends AbstractScreenMonitor {

  private float threshold;

  public HistScreenMonitor(File outDir, long lifetime, float threshold, int ssThreads,
      int ioThreads,
      int timeUpdateInterval) {
    super(outDir, lifetime, ssThreads, ioThreads, timeUpdateInterval);
    this.threshold = threshold;
  }

  @Override
  AbstractImageHandler getImageHandler(BufferedImage img0, BufferedImage img1, Mat mask,
      File output) {
    return new HistImageHandler(img0, img1, mask, output, threshold);
  }
}
