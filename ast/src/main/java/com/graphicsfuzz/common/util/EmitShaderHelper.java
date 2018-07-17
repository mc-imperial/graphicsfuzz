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

import com.graphicsfuzz.common.ast.TranslationUnit;
import com.graphicsfuzz.common.glslversion.ShadingLanguageVersion;
import com.graphicsfuzz.common.tool.PrettyPrinterVisitor;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Optional;
import java.util.function.Supplier;

public final class EmitShaderHelper {

  private EmitShaderHelper() {
    // Utility class
  }

  public static void emitDefines(PrintStream out, ShadingLanguageVersion version,
        ShaderKind shaderKind,
        Supplier<StringBuilder> extraMacros,
        Optional<String> license) {
    out.print(getDefinesString(version,
          shaderKind,
          extraMacros, license).toString());
  }

  public static StringBuilder getDefinesString(ShadingLanguageVersion version,
        ShaderKind shaderKind,
        Supplier<StringBuilder> extraMacros,
        Optional<String> license) {
    final StringBuilder sb = new StringBuilder();
    sb.append("#version " + version.getVersionString() + "\n");
    if (license.isPresent()) {
      sb.append("\n" + License.MIT_LICENSE + "\n");
      sb.append("//\n");
      sb.append("// Adapted from an original shader with copyright and license as follows:\n");
      sb.append("//\n");
      sb.append(license.get() + "\n");
    }
    if (version.isWebGl()) {
      sb.append("//WebGL\n");
    }
    sb.append("\n");
    sb.append("#ifdef GL_ES\n");
    sb.append("#ifdef GL_FRAGMENT_PRECISION_HIGH\n");
    sb.append("precision highp float;\n");
    sb.append("precision highp int;\n");
    sb.append("#else\n");
    sb.append("precision mediump float;\n");
    sb.append("precision mediump int;\n");
    sb.append("#endif\n");
    sb.append("#endif\n");
    sb.append("\n");
    sb.append(extraMacros.get());
    return sb;
  }

  public static void emitShader(ShadingLanguageVersion shadingLanguageVersion,
        ShaderKind shaderKind,
        TranslationUnit shader,
        Optional<String> license,
        PrintStream stream,
        int indentationWidth,
        Supplier<String> newlineSupplier,
        Supplier<StringBuilder> extraMacros) {
    emitDefines(stream, shadingLanguageVersion, shaderKind, extraMacros, license);
    PrettyPrinterVisitor ppv = new PrettyPrinterVisitor(stream, indentationWidth, newlineSupplier);
    ppv.visit(shader);
    stream.close();
  }

  public static void emitShader(ShadingLanguageVersion shadingLanguageVersion,
        ShaderKind shaderKind,
        TranslationUnit shader,
        Optional<String> license,
        File outputFile,
        int indentationWidth,
        Supplier<String> newlineSupplier,
        Supplier<StringBuilder> extraMacros) throws FileNotFoundException {
    emitShader(shadingLanguageVersion, shaderKind, shader, license,
          new PrintStream(new FileOutputStream(outputFile)),
          indentationWidth,
          newlineSupplier,
          extraMacros);
  }

  public static void emitShader(ShadingLanguageVersion shadingLanguageVersion,
        ShaderKind shaderKind,
        TranslationUnit shader,
        Optional<String> license,
        File outputFile) throws FileNotFoundException {
    emitShader(shadingLanguageVersion, shaderKind, shader, license,
        new PrintStream(new FileOutputStream(outputFile)),
        PrettyPrinterVisitor.DEFAULT_INDENTATION_WIDTH,
        PrettyPrinterVisitor.DEFAULT_NEWLINE_SUPPLIER,
        () -> new StringBuilder());
  }

}
