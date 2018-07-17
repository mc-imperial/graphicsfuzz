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

public class VariableIdentifierExpr extends Expr {

  private String name;

  public VariableIdentifierExpr(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public void accept(IAstVisitor visitor) {
    visitor.visitVariableIdentifierExpr(this);
  }

  @Override
  public VariableIdentifierExpr clone() {
    return new VariableIdentifierExpr(name);
  }

  @Override
  public boolean hasChild(IAstNode child) {
    return false;
  }

  @Override
  public Expr getChild(int index) {
    throw new IndexOutOfBoundsException("VariableIdentifierExpr has no children");
  }

  @Override
  public void setChild(int index, Expr expr) {
    throw new IndexOutOfBoundsException("VariableIdentifierExpr has no children");
  }

  @Override
  public int getNumChildren() {
    return 0;
  }

}
