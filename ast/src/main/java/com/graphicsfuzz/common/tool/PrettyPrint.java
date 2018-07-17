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

package com.graphicsfuzz.common.tool;

import com.graphicsfuzz.common.ast.TranslationUnit;
import com.graphicsfuzz.common.util.ParseHelper;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

class PrettyPrint {

  private static Namespace parse(String[] args) {
    ArgumentParser parser = ArgumentParsers.newArgumentParser("PrettyPrint")
        .defaultHelp(true)
        .description("Pretty print a shader.");

    // Required arguments
    parser.addArgument("shader")
        .help("Path of shader to be pretty-printed.")
        .type(File.class);

    parser.addArgument("output")
        .help("Target file name.")
        .type(String.class);

    // Optional arguments
    parser.addArgument("--glsl_version")
        .help("Version of GLSL to target.")
        .type(String.class);

    try {
      return parser.parseArgs(args);
    } catch (ArgumentParserException exception) {
      exception.getParser().handleError(exception);
      System.exit(1);
      return null;
    }

  }

  public static void main(String[] args) throws IOException, InterruptedException {

    Namespace ns = parse(args);

    try {

      long startTime = System.currentTimeMillis();
      TranslationUnit tu = ParseHelper.parse(new File(ns.getString("shader")), false);
      long endTime = System.currentTimeMillis();

      prettyPrintShader(ns, tu);

      System.err.println("Time for parsing: " + (endTime - startTime));
    } catch (Throwable exception) {
      exception.printStackTrace();
      System.exit(1);
    }

  }

  private static void prettyPrintShader(Namespace ns, TranslationUnit tu)
      throws FileNotFoundException {
    PrintStream stream = new PrintStream(new FileOutputStream(
        new File(ns.getString("output"))));
    if (getGlslVersion(ns) != null) {
      throw new RuntimeException();
      //Helper.emitDefines(stream, new ShadingLanguageVersion(getGlslVersion(ns), false),
      //    false);
    }
    PrettyPrinterVisitor ppv = new PrettyPrinterVisitor(stream);
    ppv.visit(tu);
    stream.close();
  }

  private static String getGlslVersion(Namespace ns) {
    return ns.getString("glsl_version");
  }

}
