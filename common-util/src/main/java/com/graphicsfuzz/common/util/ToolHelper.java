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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ToolHelper {

  public static ExecResult runValidatorOnShader(ExecHelper.RedirectType redirectType, File file)
        throws IOException, InterruptedException {
    return new ExecHelper().exec(
          redirectType,
          null,
          false,
          ToolPaths.glslangValidator(),
          file.toString());
  }

  public static ExecResult runShaderTranslatorOnShader(ExecHelper.RedirectType redirectType,
        File file,
        String arg)
        throws IOException, InterruptedException {
    return new ExecHelper().exec(
          redirectType,
          null,
          false,
          ToolPaths.shaderTranslator(),
          arg,
          file.toString());
  }

  public static ExecResult runGenerateImageOnShader(ExecHelper.RedirectType redirectType,
        File fragmentShader, File imageOutput, boolean skipRender)
        throws IOException, InterruptedException {
    List<String> command = new ArrayList<>(Arrays.asList(
          ToolPaths.getImageGlfw(),
          fragmentShader.toString(),
          "--output", imageOutput.toString()));

    if (skipRender) {
      command.add("--exit_linking");
    }

    return new ExecHelper().exec(
          redirectType,
          null,
          false,
          command.toArray(new String[]{}));
  }

  public static ExecResult runSwiftshaderOnShader(ExecHelper.RedirectType redirectType,
        File fragmentShader, File imageOutput, boolean skipRender)
        throws IOException, InterruptedException {
    List<String> command = new ArrayList<>(Arrays.asList(
          ToolPaths.getImageEglSwiftshader(),
          fragmentShader.toString(),
          "--output", imageOutput.toString()));

    if (skipRender) {
      command.add("--exit_linking");
    }

    return new ExecHelper().exec(
          redirectType,
          null,
          false,
          command.toArray(new String[]{}));
  }

}
