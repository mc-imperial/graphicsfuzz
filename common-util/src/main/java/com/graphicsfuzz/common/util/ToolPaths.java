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
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ToolPaths {

  public static String glslangValidator() {
    return Paths.get(getBinDir(), "glslangValidator").toString();
  }

  public static String shaderTranslator() {
    return Paths.get(getBinDir(), "shader_translator").toString();
  }

  public static String histogramDiff() {
    return Paths.get(getPythonDir(), "utilities", "histogram_diff.py").toString();
  }

  public static String getGlInfo() {
    return Paths.get(ToolPaths.getBinDir(), "get_gl_info").toString();
  }

  public static String getImageGlfw() {
    return Paths.get(ToolPaths.getBinDir(), "get_image_glfw").toString();
  }

  public static String getImageEglSwiftshader() {
    return Paths.get(ToolPaths.getBinDir(), "swiftshader", "get_image_egl").toString();
  }

  public static String getPythonDir() {
    return Paths.get(ToolPaths.getInstallDirectory(), "python").toString();
  }

  public static String getPythonDriversDir() {
    return Paths.get(getPythonDir(), "drivers").toString();
  }

  public static String getTrivialVert() {
    return Paths.get(ToolPaths.getInstallDirectory(), "shaders", "trivial.vert").toString();
  }

  public static String getStaticDir() {
    return Paths.get(ToolPaths.getInstallDirectory(), "server-static").toString();
  }

  public static String getBinDir() {
    String osName = System.getProperty("os.name").split(" ")[0];
    File jarDir = getJarDirectory();
    if (isRunningFromIde(jarDir)) {
      return Paths.get(
            getSourceRoot(jarDir),
            "assembly-binaries",
            "target",
            "assembly-binaries-1.0",
            "bin",
            osName).toString();
    }
    return Paths.get(getInstallDirectory(), "bin", osName).toString();
  }

  public static String getSourceRoot(File jarDir) {
    if (!isRunningFromIde(jarDir)) {
      throw new IllegalStateException();
    }
    return jarDir.getParentFile().getParentFile().toString();
  }

  public static File getJarDirectory() {
    try {
      File file = new File(ToolPaths.class.getProtectionDomain()
            .getCodeSource()
            .getLocation()
            .toURI()).getAbsoluteFile().getParentFile();
      return file;
    } catch (URISyntaxException exception) {
      throw new RuntimeException(exception);
    }
  }

  public static boolean isRunningFromIde(File jarDir) {
    return jarDir.getName().equals("target");
  }

  public static String getInstallDirectory() {
    File jarDir = getJarDirectory();

    if (isRunningFromIde(jarDir)) {
      // We are probably running from the IDE.
      // Return appropriate install directory depending on whether or not we have the private
      // repository available.
      final Path gfPrivatePath = Paths.get(
          getSourceRoot(jarDir),
          "repos",
          "gf-private",
          "assembly",
          "target",
          "assembly-1.0");
      if (Files.exists(gfPrivatePath)) {
        return gfPrivatePath.toString();
      }
      return Paths.get(
          getSourceRoot(jarDir),
          "assembly-public",
          "target",
          "assembly-public-1.0").toString();
    }

    return jarDir.getParentFile().toString();
  }

}
