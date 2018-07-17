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
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class UnaryExprTest {

  private UnaryExpr expr;

  @Before
  public void setup() {
    expr = new UnaryExpr(new VariableIdentifierExpr("x"), UnOp.BNEG);
  }

  @Test
  public void getExpr() throws Exception {
    assertEquals("~ x", expr.getText());
  }

  @Test
  public void getOp() throws Exception {
    assertEquals(UnOp.BNEG, expr.getOp());
  }

  @Test
  public void testClone() throws Exception {
    UnaryExpr theClone = expr.clone();
    assertFalse(expr == theClone);
    assertEquals(theClone.getText(), expr.getText());
  }

  @Test
  public void getChild() throws Exception {
    assertTrue(expr.getChild(0) instanceof VariableIdentifierExpr);
    assertEquals("x", expr.getChild(0).getText());

  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void getChildBad() throws Exception {
    expr.getChild(1);
  }

  @Test
  public void setChild() throws Exception {
    assertEquals("~ x", expr.getText());
    expr.setChild(0, new VariableIdentifierExpr("y"));
    assertEquals("~ y", expr.getText());
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void setChildBad() throws Exception {
    expr.setChild(-1, new IntConstantExpr("3"));
  }

  @Test
  public void getNumChildren() throws Exception {
    assertEquals(1, expr.getNumChildren());
  }

}