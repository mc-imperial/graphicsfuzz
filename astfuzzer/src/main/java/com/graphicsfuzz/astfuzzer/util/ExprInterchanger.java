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

package com.graphicsfuzz.astfuzzer.util;

import com.graphicsfuzz.common.ast.expr.Expr;
import java.util.List;

/**
 * The motivation for this interface is to provide a means for converting between expressions that
 * have the same signature, but that are represented by different AST nodes.
 *
 * <p>For example, the + operator and the min function can both be applied to float arguments,
 * in each case returning a float, but + is represented by a binary expression node, while min
 * is represented by a function call.
 */
public interface ExprInterchanger {

  /** .
   * @param args The arguments to the source expression.  For example, if the source expression was
   *        1 + 2, args would be [ 1, 2 ].
   * @return The interchanged expression, which will have the same arguments as the original. For
   *         example, if the source was 1 + 2 and the resulting expression is a call to "min",
   *         the result will be min(1, 2).
   */
  public Expr interchangeExpr(List<Expr> args);

}
