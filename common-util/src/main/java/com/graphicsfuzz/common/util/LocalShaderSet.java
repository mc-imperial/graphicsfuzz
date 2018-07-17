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

import com.graphicsfuzz.alphanumcomparator.AlphanumComparator;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LocalShaderSet implements IShaderSet {

  private final File shaderSetDirectory;

  public LocalShaderSet(File shaderSetDirectory) {
    this.shaderSetDirectory = shaderSetDirectory;
  }

  @Override
  public List<File> getVariants() {
    File[] variants =
        shaderSetDirectory.listFiles(pathname -> pathname.isFile()
            && pathname.getName().startsWith("variant")
            && pathname.getName().endsWith(".frag"));

    Arrays.sort(variants, (o1, o2) -> new AlphanumComparator().compare(o1.getName(), o2.getName()));

    return new ArrayList<>(Arrays.asList(variants));
  }

  @Override
  public File getReference() {
    File[] referenceFiles = shaderSetDirectory
        .listFiles(pathname -> pathname.isFile() && pathname.getName()
            .equals(REFERENCE_FILENAME));
    assert referenceFiles.length <= 1;
    if (referenceFiles.length == 0) {
      throw new RuntimeException(
          "Failed to find reference shader in shader set: " + shaderSetDirectory);
    }
    return referenceFiles[0];
  }

  @Override
  public String getName() {
    return shaderSetDirectory.getName();
  }

  @Override
  public String toString() {
    return getName();
  }

}
