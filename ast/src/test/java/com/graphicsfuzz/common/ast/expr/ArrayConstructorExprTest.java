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

import com.graphicsfuzz.common.ast.decl.ArrayInfo;
import com.graphicsfuzz.common.ast.type.ArrayType;
import com.graphicsfuzz.common.ast.type.BasicType;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;

public class ArrayConstructorExprTest {

  private ArrayConstructorExpr arrayConstructor;

  @Before
  public void setup() {
    TypeConstructorExpr temp = new TypeConstructorExpr(
        "vec4", new FloatConstantExpr("0.0"));
    arrayConstructor = new ArrayConstructorExpr(new ArrayType(
        BasicType.VEC4,
        new ArrayInfo(3)),
        Arrays.asList(temp.clone(), temp.clone(), temp.clone())
    );
  }

  @Test
  public void getArrayType() throws Exception {
    assertEquals(BasicType.VEC4, arrayConstructor.getArrayType().getBaseType());
  }

  @Test
  public void getArgs() throws Exception {
    assertEquals(3, arrayConstructor.getArgs().size());
    for (Expr arg : arrayConstructor.getArgs()) {
      assertEquals("vec4(0.0)", arg.getText());
    }
  }

  @Test
  public void testClone() throws Exception {
    ArrayConstructorExpr theClone = arrayConstructor.clone();
    assertFalse(arrayConstructor == theClone);
    assertEquals(theClone.getText(), arrayConstructor.getText());
  }

  @Test
  public void getAndSetChild() throws Exception {
    for (int i = 0; i < arrayConstructor.getNumChildren(); i++) {
      assertEquals("vec4(0.0)", arrayConstructor.getChild(i).getText());
    }
    arrayConstructor.setChild(1, new VariableIdentifierExpr("x"));
    arrayConstructor.setChild(2, new VariableIdentifierExpr("y"));
    assertEquals("vec4(0.0)", arrayConstructor.getChild(0).getText());
    assertEquals("x", arrayConstructor.getChild(1).getText());
    assertEquals("y", arrayConstructor.getChild(2).getText());
  }

  @Test
  public void getNumChildren() throws Exception {
    assertEquals(3, arrayConstructor.getNumChildren());
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void getChildBad() {
    arrayConstructor.getChild(-1);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void setChildBad() {
    arrayConstructor.setChild(3, new VariableIdentifierExpr("z"));
  }

}