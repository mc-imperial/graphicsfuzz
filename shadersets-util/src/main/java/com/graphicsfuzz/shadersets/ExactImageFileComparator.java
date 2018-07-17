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
import org.apache.commons.io.FileUtils;

public class ExactImageFileComparator implements IImageFileComparator {

  private final boolean identicalIsInteresting;

  public ExactImageFileComparator(boolean identicalIsInteresting) {
    this.identicalIsInteresting = identicalIsInteresting;
  }


  @Override
  public boolean areFilesInteresting(File reference, File variant) {
    try {
      boolean equalContent = FileUtils.contentEquals(reference, variant);
      if (!equalContent && identicalIsInteresting) {
        System.err.println("Not interesting: images do not match");
        return false;
      }
      if (equalContent && !identicalIsInteresting) {
        System.err.println("Not interesting: images match");
        return false;
      }
      return true;
    } catch (IOException exception) {
      System.err.println("Not interesting: exception while comparing files - "
          + exception.getMessage());
      return false;
    }
  }
}
