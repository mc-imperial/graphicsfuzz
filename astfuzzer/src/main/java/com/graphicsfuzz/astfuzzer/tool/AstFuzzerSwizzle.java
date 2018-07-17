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
import com.graphicsfuzz.common.ast.expr.BinaryExpr;
import com.graphicsfuzz.common.ast.expr.Expr;
import com.graphicsfuzz.common.ast.expr.FunctionCallExpr;
import com.graphicsfuzz.common.ast.expr.TernaryExpr;
import com.graphicsfuzz.common.ast.expr.TypeConstructorExpr;
import com.graphicsfuzz.common.ast.type.Type;
import com.graphicsfuzz.common.ast.visitors.StandardVisitor;
import com.graphicsfuzz.common.glslversion.ShadingLanguageVersion;
import com.graphicsfuzz.common.typing.Typer;
import com.graphicsfuzz.common.util.IRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AstFuzzerSwizzle extends AstFuzzer {

  public AstFuzzerSwizzle(ShadingLanguageVersion shadingLanguageVersion, IRandom random) {
    super(shadingLanguageVersion, random);
  }

  @Override
  public TranslationUnit generateNewShader(TranslationUnit tu) {

    TranslationUnit result = tu.cloneAndPatchUp();
    Typer typer = new Typer(result, getShadingLanguageVersion());

    new StandardVisitor() {

      @Override
      public void visitFunctionCallExpr(FunctionCallExpr functionCallExpr) {
        super.visitFunctionCallExpr(functionCallExpr);
        swizzleExpr(functionCallExpr, typer);
      }

      @Override
      public void visitBinaryExpr(BinaryExpr binaryExpr) {
        super.visitBinaryExpr(binaryExpr);
        if (!binaryExpr.getOp().isSideEffecting()) {
          swizzleExpr(binaryExpr, typer);
        }
      }

      @Override
      public void visitTernaryExpr(TernaryExpr ternaryExpr) {
        super.visitTernaryExpr(ternaryExpr);
        swizzleExpr(ternaryExpr, typer);
      }

      @Override
      public void visitTypeConstructorExpr(TypeConstructorExpr typeConstructorExpr) {
        super.visitTypeConstructorExpr(typeConstructorExpr);
        swizzleExpr(typeConstructorExpr, typer);
      }

    }.visit(result);
    return result;
  }

  private void swizzleExpr(Expr expr, Typer typer) {

    int numChildren = expr.getNumChildren();
    Map<Type, List<Expr>> expressionsByTpe = new HashMap<>();

    // Make mapping from Type to List<Expr>
    for (int i = 0; i < numChildren; i++) {
      Expr child = expr.getChild(i);

      if (typer.lookupType(child) == null) {
        continue;
      }
      if (!expressionsByTpe.containsKey(typer.lookupType(child).getWithoutQualifiers())) {
        expressionsByTpe
              .put(typer.lookupType(child).getWithoutQualifiers(), new ArrayList<>());
      }
      expressionsByTpe.get(typer.lookupType(child).getWithoutQualifiers()).add(child);
    }

    // Take each list of expressions with the same type
    for (Type type : expressionsByTpe.keySet()) {
      List<Expr> sameTypeExpressions = expressionsByTpe.get(type);

      //Take each Expr
      for (int i = 0; i < sameTypeExpressions.size(); i++) {

        int randomIndex = getRandom().nextInt(sameTypeExpressions.size());

        if (i != randomIndex) {
          final Expr oldChild = sameTypeExpressions.get(i);
          final Expr newChild = sameTypeExpressions.get(randomIndex);

          expr.replaceChild(oldChild, newChild);
        }
      }
    }


  }


}