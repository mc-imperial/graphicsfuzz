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
import com.graphicsfuzz.common.ast.type.ArrayType;
import com.graphicsfuzz.common.ast.visitors.IAstVisitor;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ArrayConstructorExpr extends Expr {

  private final ArrayType arrayType;
  private final List<Expr> args;

  public ArrayConstructorExpr(ArrayType arrayType, List<Expr> args) {
    this.arrayType = arrayType;
    this.args = args;
  }

  public ArrayType getArrayType() {
    return arrayType;
  }

  public List<Expr> getArgs() {
    return Collections.unmodifiableList(args);
  }

  @Override
  public void accept(IAstVisitor visitor) {
    visitor.visitArrayConstructorExpr(this);
  }

  @Override
  public ArrayConstructorExpr clone() {
    List<Expr> newArgs = args.stream().map(Expr::clone).collect(Collectors.toList());
    return new ArrayConstructorExpr(arrayType.clone(), newArgs);
  }

  @Override
  public boolean hasChild(IAstNode candidateChild) {
    return args.contains(candidateChild);
  }

  @Override
  public Expr getChild(int index) {
    checkBounds(index);
    return args.get(index);
  }

  @Override
  public void setChild(int index, Expr expr) {
    checkBounds(index);
    args.set(index, expr);
  }

  @Override
  public int getNumChildren() {
    return args.size();
  }

  private void checkBounds(int index) {
    if (!(index >= 0 && index < getNumChildren())) {
      throw new IndexOutOfBoundsException("No child at index " + index);
    }
  }

}
