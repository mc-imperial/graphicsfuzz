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

package com.graphicsfuzz.server;

import com.graphicsfuzz.common.util.IShaderSet;
import com.graphicsfuzz.common.util.IShaderSetExperiment;
import com.graphicsfuzz.common.util.LocalShaderSet;
import com.graphicsfuzz.common.util.LocalShaderSetExperiement;
import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Stream;

public class LocalArtifactManager implements IArtifactManager {

  private final String shaderSetsDir;
  private final String processingDir;

  public LocalArtifactManager(String shaderSetsDir, String processingDir) {
    this.shaderSetsDir = shaderSetsDir;
    this.processingDir = processingDir;
  }

  @Override
  public Stream<IShaderSet> getShaderSets() {

    if (!new File(shaderSetsDir).isDirectory()) {
      return Stream.empty();
    }

    return Arrays.stream(Paths.get(shaderSetsDir).toFile()
        .listFiles(File::isDirectory))
        .sorted((o1, o2) -> o1.getName().compareTo(o2.getName()))
        .map(file ->
            new LocalShaderSet(Paths.get(shaderSetsDir, file.getName()).toFile()));
  }

  @Override
  public IShaderSet getShaderSet(String name) {
    return new LocalShaderSet(Paths.get(shaderSetsDir, name).toFile());
  }

  @Override
  public IShaderSetExperiment getShaderSetExperiment(IShaderSet shaderSet, String token) {
    return new LocalShaderSetExperiement(
        Paths.get(
            processingDir,
            token,
            shaderSet.getName() + "_exp"
        ).toString(),
        shaderSet
    );
  }

  @Override
  public Stream<IShaderSetExperiment> getShaderSetExperiments(String token) {
    return Arrays.stream(
        new File(processingDir, token).listFiles(f -> f.getName().endsWith("_exp")))
        .map(f -> {
          String name = f.getName();
          name = name.substring(0, name.length() - 4);
          return new LocalShaderSetExperiement(f.toString(), getShaderSet(name));
        });
  }

}
