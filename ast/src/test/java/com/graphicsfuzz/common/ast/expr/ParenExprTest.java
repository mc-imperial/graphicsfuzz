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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import com.graphicsfuzz.common.ast.visitors.StandardVisitor;
import org.junit.Before;
import org.junit.Test;

public class ParenExprTest {

  private VariableIdentifierExpr x;
  private ParenExpr pe;

  @Before
  public void setUp() {
    x = new VariableIdentifierExpr("x");
    pe = new ParenExpr(x);
  }

  @Test
  public void getExpr() throws Exception {
    assertEquals(x, pe.getExpr());
  }

  @Test
  public void accept() throws Exception {
    ParenExpr nested =
      new ParenExpr(
          new ParenExpr(
              new ParenExpr(
                  new ParenExpr(
                      new ParenExpr(
                          x)))));
    assertEquals(5,
        new StandardVisitor() {

          private int numParens;

          @Override
          public void visitParenExpr(ParenExpr parenExpr) {
            super.visitParenExpr(parenExpr);
            numParens++;
          }

          public int getNumParens(ParenExpr expr) {
            numParens = 0;
            visit(expr);
            return numParens;
          }
        }.getNumParens(nested)
    );


  }

  @Test
  public void testClone() throws Exception {
    ParenExpr pe2 = pe.clone();
    assertFalse(pe == pe2);
    assertFalse(pe.getExpr() == pe2.getExpr());
    assertEquals(((VariableIdentifierExpr) pe.getExpr()).getName(), ((VariableIdentifierExpr) pe2.getExpr()).getName());
  }

  @Test
  public void getChild() throws Exception {
    assertEquals(x, pe.getChild(0));
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void getChildBad() throws Exception {
    pe.getChild(1);
  }

  @Test
  public void setChild() throws Exception {
    VariableIdentifierExpr y = new VariableIdentifierExpr("y");
    pe.setChild(0, y);
    assertEquals(y, pe.getChild(0));
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void setChildBad() throws Exception {
    VariableIdentifierExpr y = new VariableIdentifierExpr("y");
    pe.setChild(1, y);
  }

  @Test
  public void getNumChildren() throws Exception {
    assertEquals(1, pe.getNumChildren());
  }

}