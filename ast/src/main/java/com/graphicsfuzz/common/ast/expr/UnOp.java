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

public enum UnOp implements Op {
  POST_INC, POST_DEC, PRE_INC, PRE_DEC, PLUS, MINUS, BNEG, LNOT;

  /**
   * Produce text representation of the unary operator.
   * @return Text representation
   */
  @Override
  public String getText() {
    switch (this) {
      case BNEG:
        return "~";
      case LNOT:
        return "!";
      case MINUS:
        return "-";
      case PLUS:
        return "+";
      case POST_DEC:
        return "--";
      case POST_INC:
        return "++";
      case PRE_DEC:
        return "--";
      default:
      assert this == PRE_INC : "Unknown unary operator: " + this;
        return "++";
    }
  }

  @Override
  public boolean isSideEffecting() {
    return this == PRE_INC || this == PRE_DEC || this == POST_INC || this == POST_DEC;
  }

}
