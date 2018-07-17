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

package com.graphicsfuzz.shadersets;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_imgcodecs;
import org.bytedeco.javacpp.opencv_imgproc;

/**
 * Stores data about an image; right now its file and histogram.
 * Could be extended in due course with e.g. PSNR
 */
public class ImageData {

  public final File imageFile;
  private final opencv_core.Mat imageMat;
  private final opencv_core.Mat histogram;

  public ImageData(File imageFile) throws FileNotFoundException {
    this.imageFile = imageFile;
    this.imageMat = opencv_imgcodecs.imread(imageFile.getAbsolutePath());
    opencv_imgproc.cvtColor(this.imageMat, this.imageMat, opencv_imgproc.COLOR_BGR2HSV);
    this.histogram = ImageUtil.getHistogram(imageFile.getAbsolutePath());
  }

  public ImageData(String imageFileName) throws FileNotFoundException {
    this(new File(imageFileName));
  }

  public Map<String, Double> getImageDiffStats(ImageData other) {
    Map<String, Double> result = new HashMap<>();
    result.put("histogramDistance", ImageUtil.compareHistograms(this.histogram, other.histogram));
    result.put("psnr", opencv_core.PSNR(this.imageMat, other.imageMat));
    return result;
  }

}
