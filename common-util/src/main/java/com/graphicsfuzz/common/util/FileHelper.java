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

package com.graphicsfuzz.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.apache.commons.io.FilenameUtils;

public class FileHelper {

  public static File fileWithAppendedString(File file, String extra) {
    String ext = FilenameUtils.getExtension(file.toString());
    String basename = FilenameUtils.removeExtension(file.toString());
    return new File(basename + extra + "." + ext);
  }

  public static String firstLine(File file) throws IOException {
    try (BufferedReader r = new BufferedReader(new FileReader(file));) {
      return r.readLine();
    }
  }

  public static File replaceExtension(File file, String ext) {
    return new File(FilenameUtils.removeExtension(file.toString()) + ext);
  }

  public static File replaceDir(File file, File dir) {
    return new File(dir, file.getName());
  }

  public static void checkExists(File file) throws FileNotFoundException {
    if (!file.exists()) {
      throw new FileNotFoundException("Could not find: " + file);
    }
  }

  public static void checkExistsOrNull(File file) throws FileNotFoundException {
    if (file != null) {
      checkExists(file);
    }
  }

}
