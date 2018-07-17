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

public class TernaryExprTest {

  private VariableIdentifierExpr x;
  private VariableIdentifierExpr y;
  private VariableIdentifierExpr z;

  @Before
  public void setUp() {
    x = new VariableIdentifierExpr("x");
    y = new VariableIdentifierExpr("y");
    z = new VariableIdentifierExpr("z");
  }

  private TernaryExpr makeTernary() {
    return new TernaryExpr(x, y, z);
  }

  @Test
  public void getTest() throws Exception {
    TernaryExpr te = makeTernary();
    assertEquals(x, te.getTest());
    assertEquals(x, te.getChild(0));
  }

  @Test
  public void getThenExpr() throws Exception {
    TernaryExpr te = makeTernary();
    assertEquals(y, te.getThenExpr());
    assertEquals(y, te.getChild(1));
  }

  @Test
  public void getElseExpr() throws Exception {
    TernaryExpr te = makeTernary();
    assertEquals(z, te.getElseExpr());
    assertEquals(z, te.getChild(2));
  }

  @Test
  public void accept() throws Exception {
    TernaryExpr te = new TernaryExpr(
        makeTernary().clone(),
        makeTernary().clone(),
        makeTernary().clone());
    assertEquals(4,
        new StandardVisitor() {
          private int numTernaries;

          @Override
          public void visitTernaryExpr(TernaryExpr ternaryExpr) {
            super.visitTernaryExpr(ternaryExpr);
            numTernaries++;
          }

          public int getNumTernaries(TernaryExpr node) {
            numTernaries = 0;
            visit(node);
            return numTernaries;
          }

        }.getNumTernaries(te)
    );
  }

  @Test
  public void testClone() throws Exception {
    TernaryExpr te = makeTernary();
    TernaryExpr te2 = te.clone();
    assertFalse(te == te2);
    assertFalse(te.getTest() == te2.getTest());
    assertFalse(te.getThenExpr() == te2.getThenExpr());
    assertFalse(te.getElseExpr() == te2.getElseExpr());

    assertEquals(((VariableIdentifierExpr) te.getTest()).getName(), ((VariableIdentifierExpr) te2.getTest()).getName());
    assertEquals(((VariableIdentifierExpr) te.getThenExpr()).getName(), ((VariableIdentifierExpr) te2.getThenExpr()).getName());
    assertEquals(((VariableIdentifierExpr) te.getElseExpr()).getName(), ((VariableIdentifierExpr) te2.getElseExpr()).getName());
  }

  @Test
  public void setChild() throws Exception {
    TernaryExpr te = makeTernary();
    te.setChild(0, z);
    te.setChild(1, x);
    te.setChild(2, y);
    assertEquals(z, te.getTest());
    assertEquals(x, te.getThenExpr());
    assertEquals(y, te.getElseExpr());
  }

  @Test
  public void getNumChildren() throws Exception {
    assertEquals(3, makeTernary().getNumChildren());
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void getChildBad() {
    makeTernary().getChild(3);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void setChildBad() {
    makeTernary().setChild(3, x);
  }

}