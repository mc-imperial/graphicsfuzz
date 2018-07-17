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

package com.graphicsfuzz.astfuzzer.tool;

import com.graphicsfuzz.common.ast.TranslationUnit;
import com.graphicsfuzz.common.glslversion.ShadingLanguageVersion;
import com.graphicsfuzz.common.tool.PrettyPrinterVisitor;
import com.graphicsfuzz.common.util.ExecHelper.RedirectType;
import com.graphicsfuzz.common.util.ExecResult;
import com.graphicsfuzz.common.util.ParseHelper;
import com.graphicsfuzz.common.util.ParseTimeoutException;
import com.graphicsfuzz.common.util.RandomWrapper;
import com.graphicsfuzz.common.util.ToolHelper;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.util.List;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

public class MainFuzzer {

  private static Namespace parse(String[] args) {

    ArgumentParser parser = ArgumentParsers.newArgumentParser("AstFuzzer")
          .defaultHelp(true)
          .description("Takes a shader as input, returns a list of variant shaders, "
                + "i.e shaders which have a different AST.");

    // Required arguments
    parser.addArgument("fragment_shader")
          .help("Path of fragment shader to be fuzzed.")
          .type(File.class);

    // Optional arguments
    parser.addArgument("--number_of_variants")
          .help("How many variant shaders you want to generate.")
          .setDefault(25)
          .type(Integer.class);
    parser.addArgument("--keep_bad_shaders")
          .help("Keep the generated shaders which do not compile.")
          .setDefault(false)
          .type(Boolean.class);

    parser.addArgument("--output_prefix")
          .help("Prefix of target file name, e.g. \"foo\" if shader is to be \"foo.frag\".")
          .setDefault("")
          .type(String.class);

    try {
      return parser.parseArgs(args);
    } catch (ArgumentParserException exception) {
      exception.getParser().handleError(exception);
      System.exit(1);
      return null;
    }

  }

  /**
   * Calls AstFuzzer and writes the generated shaders in files.
   *
   * @param args program arguments list.
   */
  public static void main(String[] args) {
    Namespace ns = parse(args);

    try {
      final File fragmentShader = ns.get("fragment_shader");
      final Integer numberOfVariants = ns.get("number_of_variants");
      final String outputPrefix = ns.get("output_prefix");
      final Boolean keepBadShader = ns.get("keep_bad_shaders");

      generateVariants(
          fragmentShader, new File("."), numberOfVariants, outputPrefix, keepBadShader);
    } catch (Exception exception) {
      exception.printStackTrace();
    }
  }

  public static void generateVariants(
      File fragmentShader,
      File workDir,
      Integer numberOfVariants,
      String outputPrefix, Boolean keepBadShader)
      throws IOException, ParseTimeoutException, InterruptedException, URISyntaxException {

    File outputFolder = new File(workDir, "output");

    try {
      outputFolder.mkdir();
    } catch (Exception exception) {
      System.err.print(exception.getMessage());
    }

    ShadingLanguageVersion shadingLanguageVersion =
        ShadingLanguageVersion.getGlslVersionFromShader(fragmentShader);
    TranslationUnit initialTu = ParseHelper.parse(fragmentShader, true);
    AstFuzzer fuzzer = new AstFuzzerChangeAnythingMatching(shadingLanguageVersion,
          new RandomWrapper());
    List<TranslationUnit> variants = fuzzer.generateShaderVariations(initialTu, numberOfVariants);

    for (int i = 0; i < numberOfVariants; i++) {

      File outputFile = new File(outputFolder, outputPrefix
          + fragmentShader.toString().split("\\.frag")[0] + "_" + i + ".frag");
      if (!outputFile.exists()) {
        new File(outputFile.getParent()).mkdirs();
        outputFile.createNewFile();
      }
      writeFile(variants.get(i), shadingLanguageVersion, outputFile);

      File json = new File(outputFolder, outputPrefix
            + fragmentShader.toString().split("\\.frag")[0] + "_" + i + ".json");
      json.createNewFile();
      InputStream is = MainFuzzer.class.getClassLoader().getResourceAsStream("default.json");
      java.util.Scanner scanner = new java.util.Scanner(is, "UTF-8").useDelimiter("\\A");
      PrintWriter pw = new PrintWriter(json);
      while (scanner.hasNext()) {
        pw.append(scanner.next());
      }
      pw.close();
      checkShaderValidity(outputFile, json, keepBadShader);
    }
  }

  private static void checkShaderValidity(File outputFile, File jsonFile, Boolean keepBadShader)
        throws IOException, InterruptedException {

    ExecResult execResult = ToolHelper
          .runValidatorOnShader(RedirectType.TO_BUFFER, outputFile);

    //If the file does not compile
    if (execResult.res != 0) {
      jsonFile.delete();

      if (keepBadShader) {
        try {
          outputFile.renameTo(new File(outputFile.toString().split("\\.frag")[0]
                + "_bad.frag"));
        } catch (Exception exeception) {
          System.err.print(exeception.getMessage());
        }
      } else {
        try {
          outputFile.delete();
        } catch (Exception exeception) {
          System.err.print(exeception.getMessage());
        }
      }
    }

  }


  private static void writeFile(TranslationUnit tu,
      ShadingLanguageVersion shadingLanguageVersion, File outputFile)
        throws FileNotFoundException {
    PrintStream ps = new PrintStream(outputFile);
    assert false; // TODO: need to decide how to handle this
    //Helper.emitDefines(ps, shadingLanguageVersion, true);

    new PrettyPrinterVisitor(ps).visit(tu);
    ps.flush();
    ps.close();
  }
}
