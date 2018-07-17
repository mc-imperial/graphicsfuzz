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

package com.graphicsfuzz.security.tool;

import java.io.File;
import org.apache.commons.io.FilenameUtils;

public abstract class FileGetter {

  //Gets a unique file (e.g. if file "0.png" is wanted but already exists, returns "0 (1).png")
  public static File getUniqueFile(File dir, String fileName, String extension) {
    File outFile = new File(dir, fileName + extension);
    int count = 1;
    while (outFile.exists()) {
      outFile = new File(dir, fileName + "_" + count + extension);
      count++;
    }
    return outFile;
  }

  public static File getUniqueFile(String fileName, String extension) {
    File dir = new File(".");
    return getUniqueFile(dir, fileName, extension);
  }

  public static File getUniqueFile(String file) {
    String fileName = FilenameUtils.removeExtension(file);
    String extension = "." + FilenameUtils.getExtension(file);
    return getUniqueFile(fileName, extension);
  }

  public static File getUniqueFile(File file) {
    File dir = file.getParentFile();
    String fileName = FilenameUtils.removeExtension(file.getName());
    String extension = "." + FilenameUtils.getExtension(file.getName());
    return getUniqueFile(dir, fileName, extension);
  }

  public static String getUniqueFileName(File dir, String fileName, String extension) {
    return FilenameUtils.removeExtension(getUniqueFile(dir, fileName, extension).getName());
  }

  public static String getUniqueFileName(String fileName, String extension) {
    return getUniqueFileName(new File("."), fileName, extension);
  }

  public static String getUniqueFileName(String file) {
    String fileName = FilenameUtils.removeExtension(file);
    String extension = "." + FilenameUtils.getExtension(file);
    return getUniqueFileName(fileName, extension);
  }
}
