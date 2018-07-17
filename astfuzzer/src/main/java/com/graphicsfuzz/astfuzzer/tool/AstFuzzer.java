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
import com.graphicsfuzz.common.util.IRandom;
import com.graphicsfuzz.common.util.ParseTimeoutException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public abstract class AstFuzzer {

  private final InterchangeablesGroupedBySignature functionsBySignature;
  private final ShadingLanguageVersion shadingLanguageVersion;
  private final IRandom random;

  /**
   * Constructor.
   *
   * @param shadingLanguageVersion The version of glsl you want to generate for.
   * @param random Used to generate fuzzed in a random manner.
   */
  public AstFuzzer(ShadingLanguageVersion shadingLanguageVersion,
        IRandom random) {
    this.shadingLanguageVersion = shadingLanguageVersion;
    this.functionsBySignature = new InterchangeablesGroupedBySignature(shadingLanguageVersion);
    this.random = random;
  }

  IRandom getRandom() {
    return random;
  }

  ShadingLanguageVersion getShadingLanguageVersion() {
    return shadingLanguageVersion;
  }

  InterchangeablesGroupedBySignature getFunctionLists() {
    return functionsBySignature;
  }

  /**
   * Generates "numberOfVariants" TranslationUnit variations based on the initial TranslationUnit.
   *
   * @param tu The TranslationUnit representation of the shader to be modified
   */
  public final List<TranslationUnit> generateShaderVariations(TranslationUnit tu,
        int numberOfVariants)
        throws IOException, ParseTimeoutException {

    List<TranslationUnit> result = new ArrayList<>();

    for (int i = 0; i < numberOfVariants; i++) {
      result.add(generateNewShader(tu));
    }
    return result;
  }

  public abstract TranslationUnit generateNewShader(TranslationUnit tu);
}