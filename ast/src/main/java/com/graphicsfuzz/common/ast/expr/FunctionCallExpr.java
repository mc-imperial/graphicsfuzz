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

package com.graphicsfuzz.common.ast.expr;

import com.graphicsfuzz.common.ast.IAstNode;
import com.graphicsfuzz.common.ast.visitors.IAstVisitor;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class FunctionCallExpr extends Expr {

  private String callee;
  private List<Expr> args;

  public FunctionCallExpr(String callee, List<Expr> args) {
    this.callee = callee;
    this.args = args;
  }

  public FunctionCallExpr(String callee, Expr... args) {
    this(callee, Arrays.asList(args));
  }

  public String getCallee() {
    return callee;
  }

  public void setCallee(String callee) {
    this.callee = callee;
  }

  public List<Expr> getArgs() {
    return Collections.unmodifiableList(args);
  }

  public int getNumArgs() {
    return args.size();
  }

  public Expr getArg(int index) {
    return args.get(index);
  }

  public void setArg(int index, Expr expr) {
    args.set(index, expr);
  }

  @Override
  public void accept(IAstVisitor visitor) {
    visitor.visitFunctionCallExpr(this);
  }

  @Override
  public FunctionCallExpr clone() {
    return new FunctionCallExpr(callee,
        args.stream().map(x -> x.clone()).collect(Collectors.toList()));
  }

  @Override
  public boolean hasChild(IAstNode candidateChild) {
    return args.contains(candidateChild);
  }

  @Override
  public Expr getChild(int index) {
    if (index < 0 || index >= getNumArgs()) {
      throw new IndexOutOfBoundsException("FunctionCallExpr has no child at index " + index);
    }
    return getArg(index);
  }

  @Override
  public void setChild(int index, Expr expr) {
    if (index < 0 || index >= getNumArgs()) {
      throw new IndexOutOfBoundsException("FunctionCallExpr has no child at index " + index);
    }
    args.set(index, expr);
  }

  @Override
  public int getNumChildren() {
    return getNumArgs();
  }

}
