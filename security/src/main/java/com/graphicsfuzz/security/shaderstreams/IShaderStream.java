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

package com.graphicsfuzz.security.shaderstreams;

import com.graphicsfuzz.shadersets.IShaderDispatcher;
import com.graphicsfuzz.shadersets.ShaderDispatchException;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

//A stream (list) of shaders with ability to run all shaders in the list, or run a given number of
//shaders in the list
//Extends Iterator<File> so that next() and hasNext() are also part of this interface
public interface IShaderStream extends Iterator<File> {

  //Run the current shader (using imageGenerator and saving in workDir) and iterate
  //Returns image file
  File runShaderAndIterate(IShaderDispatcher imageGenerator, File workDir)
      throws InterruptedException, IOException, ShaderDispatchException;

  //Runs current shader without iterating (as above)
  File runShader(IShaderDispatcher imageGenerator, File workDir)
      throws InterruptedException, IOException, ShaderDispatchException;

  //Run all shaders in the stream using imageGenerator (usingSwiftShader = true), save in workDir
  void runStream(IShaderDispatcher imageGenerator, File workDir)
      throws ShaderDispatchException, InterruptedException, IOException;

  //Run maxShadersRun shaders in the stream, same way as above
  void runStreamUntil(IShaderDispatcher imageGenerator, File workDir, int maxShadersRun)
      throws ShaderDispatchException, InterruptedException, IOException;

}
