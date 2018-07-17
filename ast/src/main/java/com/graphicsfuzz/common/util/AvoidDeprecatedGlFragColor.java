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
import com.graphicsfuzz.common.ast.decl.VariableDeclInfo;
import com.graphicsfuzz.common.ast.decl.VariablesDeclaration;
import com.graphicsfuzz.common.ast.expr.VariableIdentifierExpr;
import com.graphicsfuzz.common.ast.type.BasicType;
import com.graphicsfuzz.common.ast.type.LayoutQualifier;
import com.graphicsfuzz.common.ast.type.QualifiedType;
import com.graphicsfuzz.common.ast.type.TypeQualifier;
import com.graphicsfuzz.common.ast.visitors.StandardVisitor;
import java.util.Arrays;

public class AvoidDeprecatedGlFragColor extends StandardVisitor {

  private final String colorName;

  private AvoidDeprecatedGlFragColor(String colorName) {
    this.colorName = colorName;
  }

  /**
   * This *mutator* method changes all occurrences of gl_FragColor to use the given name.
   * @param tu The translation unit to be mutated.
   * @param colorName The replacement name for gl_FragColor.
   */
  public static void avoidDeprecatedGlFragColor(TranslationUnit tu,
        String colorName) {
    new AvoidDeprecatedGlFragColor(colorName).visit(tu);
    tu.addDeclaration(new VariablesDeclaration(
        new QualifiedType(BasicType.VEC4, Arrays.asList(
            new LayoutQualifier("location = 0"), TypeQualifier.SHADER_OUTPUT)),
        new VariableDeclInfo(colorName, null, null)
    ));
  }

  @Override
  public void visitVariableIdentifierExpr(VariableIdentifierExpr variableIdentifierExpr) {
    super.visitVariableIdentifierExpr(variableIdentifierExpr);
    if (variableIdentifierExpr.getName().equals(OpenGlConstants.GL_FRAG_COLOR)) {
      variableIdentifierExpr.setName(colorName);
    }
  }

}
