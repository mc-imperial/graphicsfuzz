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

import org.junit.Test;

public class VariableIdentifierExprTest {

  @Test
  public void setName() throws Exception {
    VariableIdentifierExpr x = new VariableIdentifierExpr("x");
    assertEquals("x", x.getName());
    x.setName("y");
    assertEquals("y", x.getName());
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void getChild() throws Exception {
    VariableIdentifierExpr x = new VariableIdentifierExpr("x");
    x.getChild(0);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void setChild() throws Exception {
    VariableIdentifierExpr x = new VariableIdentifierExpr("x");
    x.setChild(0, null);
  }

  @Test
  public void getNumChildren() throws Exception {
    VariableIdentifierExpr x = new VariableIdentifierExpr("x");
    assertEquals(0, x.getNumChildren());
  }

}