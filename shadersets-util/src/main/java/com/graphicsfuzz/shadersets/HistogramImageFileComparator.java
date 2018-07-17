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
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HistogramImageFileComparator implements IImageFileComparator {

  private static final Logger LOGGER = LoggerFactory.getLogger(HistogramImageFileComparator.class);

  private final double threshold;
  private final boolean above;

  public HistogramImageFileComparator(double threshold, boolean above) {
    this.threshold = threshold;
    this.above = above;
  }

  @Override
  public boolean areFilesInteresting(File reference, File variant) {
    try {
      LOGGER.info("Comparing: {} and {}.", reference, variant);
      double diff = ImageUtil.compareHistograms(
          ImageUtil.getHistogram(reference.toString()), ImageUtil.getHistogram(variant.toString()));
      boolean result = (above ? diff > threshold : diff < threshold);
      if (result) {
        LOGGER.info("Interesting");
      } else {
        LOGGER.info("Not interesting");
      }
      LOGGER.info(": difference is " + diff);
      return result;
    } catch (IOException exception) {
      LOGGER.info("Not interesting - exception occurred during histogram comparison.");
      return false;
    }
  }
}
