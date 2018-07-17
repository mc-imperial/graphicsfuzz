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

import com.graphicsfuzz.astfuzzer.util.ExprInterchanger;
import com.graphicsfuzz.common.ast.IAstNode;
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
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class AstFuzzerChangeAnythingMatching extends AstFuzzer {

  private Map<Expr, Expr> mapOfReplacements;

  public AstFuzzerChangeAnythingMatching(ShadingLanguageVersion shadingLanguageVersion,
      IRandom random) {
    super(shadingLanguageVersion, random);
  }

  @Override
  public TranslationUnit generateNewShader(TranslationUnit tu) {

    TranslationUnit result = tu.cloneAndPatchUp();
    traverseTreeAndMakeMappings(result);

    new StandardVisitor() {

      @Override
      protected <T extends IAstNode> void visitChildFromParent(Consumer<T> visitorMethod,
            T child,
            IAstNode parent) {
        super.visitChildFromParent(visitorMethod, child, parent);
        if (mapOfReplacements.containsKey(child)) {
          //some IAstNodes don't allow replaceChild
          try {
            parent.replaceChild(child, mapOfReplacements.get(child));
          } catch (Exception exception) {
            System.err.println(exception.getMessage());
          }
        }
        ;
      }

    }.visit(result);
    return result;

  }

  private void traverseTreeAndMakeMappings(TranslationUnit tu) {

    Typer typer = new Typer(tu, getShadingLanguageVersion());
    mapOfReplacements = new HashMap<>();

    new StandardVisitor() {

      @Override
      public void visitFunctionCallExpr(FunctionCallExpr functionCallExpr) {
        super.visitFunctionCallExpr(functionCallExpr);
        fuzzExpr(functionCallExpr, typer);

      }

      @Override
      public void visitBinaryExpr(BinaryExpr binaryExpr) {
        super.visitBinaryExpr(binaryExpr);
        if (!binaryExpr.getOp().isSideEffecting()) {
          fuzzExpr(binaryExpr, typer);
        }
      }

      @Override
      public void visitTernaryExpr(TernaryExpr ternaryExpr) {
        super.visitTernaryExpr(ternaryExpr);
        fuzzExpr(ternaryExpr, typer);
      }

      @Override
      public void visitTypeConstructorExpr(TypeConstructorExpr typeConstructorExpr) {
        super.visitTypeConstructorExpr(typeConstructorExpr);
        fuzzExpr(typeConstructorExpr, typer);
      }
    }.visit(tu);

  }

  private void fuzzExpr(Expr expr, Typer typer) {

    if (getRandom().nextBoolean()) {
      Expr replacement = findReplacement(expr, typer);
      mapOfReplacements.put(expr, replacement);
    }
  }

  private Expr findReplacement(Expr expr, Typer typer) {

    Type returnType = typer.lookupType(expr);
    List<Expr> args = new ArrayList<>();
    for (int i = 0; i < expr.getNumChildren(); i++) {
      args.add(expr.getChild(i));

    }
    Signature signature = new Signature(returnType,
          args.stream().map(x -> typer.lookupType(x)).collect(Collectors.toList()));

    List<ExprInterchanger> matches = getFunctionLists()
          .getInterchangeableForSignature(signature);
    if (!matches.isEmpty()) {
      int randomIndex = getRandom().nextInt(matches.size());
      return matches.get(randomIndex).interchangeExpr(args);
    }
    return expr;
  }
}
