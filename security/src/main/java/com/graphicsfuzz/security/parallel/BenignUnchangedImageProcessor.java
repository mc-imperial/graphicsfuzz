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

package com.graphicsfuzz.security.parallel;

import com.graphicsfuzz.shadersets.IImageFileComparator;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class BenignUnchangedImageProcessor implements IImageProcessor {

  private File refImage;
  private FileWriter writer;
  private IImageFileComparator comparator;
  private File image = null;
  private boolean errorsFound = false;

  public BenignUnchangedImageProcessor(File refImage, FileWriter writer,
      IImageFileComparator comparator) {
    this.refImage = refImage;
    this.writer = writer;
    this.comparator = comparator;
  }

  //Set this.image to image (no overwrite)
  @Override
  public void addImage(File image) {
    if (this.image == null) {
      this.image = image;
    }
  }

  //Set image to null
  @Override
  public void resetImages() {
    image = null;
  }

  //Process image (check for differences from refImage)
  @Override
  public void process() throws IOException {
    if (image.getPath().endsWith(".png") && comparator.areFilesInteresting(refImage, image)) {
      writer.append("Security Error: ")
          .append(image.getName())
          .append(" is different to the reference image\n");
      errorsFound = true;
    }
  }

  //Return result of process and reset errorsFound
  @Override
  public boolean getResultAndFinish() throws IOException {
    if (!errorsFound) {
      writer.append("No security errors found!");
      System.out.println("No security errors found!");
    } else {
      System.out.println("Security errors found! Mismatching benign images, see "
          + "result log file for details!");
    }
    boolean res = errorsFound;
    errorsFound = false;
    return res;
  }
}
