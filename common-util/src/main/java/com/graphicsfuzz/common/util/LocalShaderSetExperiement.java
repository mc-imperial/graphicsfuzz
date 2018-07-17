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

import java.io.File;
import java.util.Arrays;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocalShaderSetExperiement implements IShaderSetExperiment {

  private static final Logger LOGGER = LoggerFactory.getLogger(LocalShaderSetExperiement.class);

  private static final String REFERENCE_IMAGE_FILENAME = "reference.png";
  private static final String REFERENCE_TEXT_FILE_FILENAME = "reference.txt";
  private static final String VARIANT_STARTSWITH = "variant";
  private static final String IMAGE_EXT = ".png";
  private static final String TEXT_EXT = ".txt";

  private String dir;

  private final IShaderSet shaderSet;

  public LocalShaderSetExperiement(String dir, IShaderSet shaderSet) {
    this.dir = dir;
    this.shaderSet = shaderSet;
  }

  @Override
  public File getReferenceImage() {
    File res = new File(dir, REFERENCE_IMAGE_FILENAME);
    LOGGER.info("Looking for reference image: " + res);
    return res.isFile() ? res : null;
  }

  @Override
  public File getReferenceTextFile() {
    File res = new File(dir, REFERENCE_TEXT_FILE_FILENAME);
    LOGGER.info("Looking for reference text file: " + res);
    return res.isFile() ? res : null;
  }

  @Override
  public Stream<File> getVariantImages() {
    return Arrays.stream(new File(dir).listFiles())
        .filter(file ->
            file.isFile()
                && file.getName().endsWith(IMAGE_EXT)
                && file.getName().startsWith(VARIANT_STARTSWITH));
  }

  @Override
  public Stream<File> getVariantTextFiles() {
    return Arrays.stream(new File(dir).listFiles())
        .filter(file ->
            file.isFile()
                && file.getName().endsWith(TEXT_EXT)
                && file.getName().startsWith(VARIANT_STARTSWITH));
  }

  @Override
  public IShaderSet getShaderSet() {
    return shaderSet;
  }

  @Override
  public String toString() {
    return dir.toString();
  }
}
