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

package com.graphicsfuzz.serverpublic;

import com.graphicsfuzz.server.ICommandDispatcher;
import com.graphicsfuzz.server.thrift.FuzzerServiceManager;
import com.graphicsfuzz.shadersets.RunShaderSet;
import com.graphicsfuzz.shadersets.ShaderDispatchException;
import java.io.IOException;
import java.util.List;
import net.sourceforge.argparse4j.inf.ArgumentParserException;

public class PublicServerCommandDispatcher implements ICommandDispatcher {

  @Override
  public void dispatchCommand(List<String> command, FuzzerServiceManager.Iface fuzzerServiceManager)
        throws ShaderDispatchException, ArgumentParserException, InterruptedException, IOException {
    switch (command.get(0)) {
      case "run_shader_set":
        RunShaderSet.mainHelper(
              command.subList(1, command.size()).toArray(new String[0]),
              fuzzerServiceManager
        );
        break;
      default:
        throw new RuntimeException("Unknown command: " + command.get(0));
    }
  }

}
