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

import com.graphicsfuzz.common.ast.ChildDoesNotExistException;
import com.graphicsfuzz.common.ast.IAstNode;

public abstract class Expr implements IAstNode {

  @Override
  public abstract Expr clone();

  public abstract Expr getChild(int index);

  public abstract void setChild(int index, Expr expr);

  public abstract int getNumChildren();

  @Override
  public void replaceChild(IAstNode child, IAstNode newChild) {
    if (!(child instanceof Expr && newChild instanceof Expr)) {
      throw new IllegalArgumentException();
    }
    for (int i = 0; i < getNumChildren(); i++) {
      if (getChild(i) == child) {
        setChild(i, (Expr) newChild);
        return;
      }
    }
    throw new ChildDoesNotExistException(child, this);
  }

  public abstract boolean hasChild(IAstNode child);

}
