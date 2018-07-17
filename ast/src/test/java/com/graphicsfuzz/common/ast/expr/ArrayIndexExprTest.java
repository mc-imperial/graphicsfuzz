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

import org.junit.Before;
import org.junit.Test;

public class ArrayIndexExprTest {

  private ArrayIndexExpr expr;

  @Before
  public void setup() {
    expr = new ArrayIndexExpr(new VariableIdentifierExpr("A"),
        new IntConstantExpr("0"));
  }

  @Test
  public void getArray() throws Exception {
    assertEquals("A", expr.getArray().getText());
  }

  @Test
  public void getIndex() throws Exception {
    assertEquals("0", expr.getIndex().getText());
  }

  @Test
  public void testClone() throws Exception {
    ArrayIndexExpr theClone = expr.clone();
    assertFalse(expr == theClone);
    assertEquals(theClone.getText(), expr.getText());
  }

  @Test
  public void getChild() throws Exception {
    assertEquals("A", expr.getChild(0).getText());
    assertEquals("0", expr.getChild(1).getText());
  }

  @Test
  public void setChild() throws Exception {
    assertEquals("A", expr.getChild(0).getText());
    assertEquals("0", expr.getChild(1).getText());
    expr.setChild(0, new VariableIdentifierExpr("B"));
    assertEquals("B", expr.getChild(0).getText());
    assertEquals("0", expr.getChild(1).getText());
    expr.setChild(1, new IntConstantExpr("3"));
    assertEquals("B", expr.getChild(0).getText());
    assertEquals("3", expr.getChild(1).getText());
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void getChildBad() {
    expr.getChild(2);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void setChildBad() {
    expr.setChild(-1, new VariableIdentifierExpr("T"));
  }

  @Test
  public void getNumChildren() throws Exception {
    assertEquals(2, expr.getNumChildren());
  }

}