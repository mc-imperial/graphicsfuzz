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

import com.graphicsfuzz.common.util.ExecHelper.RedirectType;
import com.graphicsfuzz.common.util.ExecResult;
import com.graphicsfuzz.common.util.ToolHelper;
import com.graphicsfuzz.server.thrift.ComputeJobResult;
import com.graphicsfuzz.server.thrift.FuzzerServiceConstants;
import com.graphicsfuzz.server.thrift.ImageJobResult;
import com.graphicsfuzz.server.thrift.JobStatus;
import com.graphicsfuzz.server.thrift.ResultConstant;
import java.io.File;
import java.io.IOException;

public class LocalShaderDispatcher implements IShaderDispatcher {

  private final boolean usingSwiftshader;

  public LocalShaderDispatcher(boolean usingSwiftshader) {
    this.usingSwiftshader = usingSwiftshader;
  }

  @Override
  public ImageJobResult getImage(
      String shaderFilesPrefix,
      File tempImageFile,
      boolean skipRender) throws ShaderDispatchException, InterruptedException {

    final File fragmentShaderFile = new File(shaderFilesPrefix + ".frag");
    final File vertexShaderFile = new File(shaderFilesPrefix + ".vert");

    if (vertexShaderFile.isFile()) {
      throw new RuntimeException("Not yet supporting vertex shaders in local image generation.");
    }

    try {
      ExecResult res = usingSwiftshader
          ? ToolHelper.runSwiftshaderOnShader(RedirectType.TO_BUFFER, fragmentShaderFile,
              tempImageFile, skipRender)
          : ToolHelper.runGenerateImageOnShader(RedirectType.TO_BUFFER, fragmentShaderFile,
          tempImageFile, skipRender);

      ImageJobResult imageJobResult = new ImageJobResult();

      if (res.res == 0) {
        imageJobResult
            .setStatus(JobStatus.SUCCESS);

        return imageJobResult;
      }

      ResultConstant resultConstant = ResultConstant.ERROR;
      JobStatus status = JobStatus.UNEXPECTED_ERROR;

      if (res.res == FuzzerServiceConstants.COMPILE_ERROR_EXIT_CODE) {
        resultConstant = ResultConstant.COMPILE_ERROR;
        status = JobStatus.COMPILE_ERROR;
      } else if (res.res == FuzzerServiceConstants.LINK_ERROR_EXIT_CODE) {
        resultConstant = ResultConstant.LINK_ERROR;
        status = JobStatus.LINK_ERROR;
      }

      res.stdout.append(res.stderr);
      imageJobResult
          .setStatus(status)
          .setLog(resultConstant + "\n" + res.stdout.toString());

      return imageJobResult;

    } catch (IOException exception) {
      throw new ShaderDispatchException(exception);
    }
  }

  @Override
  public ComputeJobResult dispatchCompute(File computeShaderFile, boolean skipExecution) {
    throw new RuntimeException("Compute shaders are not supported locally.");
  }

}
