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

package com.graphicsfuzz.common.ast.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class BasicTypeTest {

  @Test
  public void testCanonicalConstant() {
    assertEquals("1", BasicType.INT.getCanonicalConstant().getText());
    assertEquals("1u", BasicType.UINT.getCanonicalConstant().getText());
    assertEquals("1.0", BasicType.FLOAT.getCanonicalConstant().getText());
    assertEquals("true", BasicType.BOOL.getCanonicalConstant().getText());
    assertEquals("uvec4(1u)", BasicType.UVEC4.getCanonicalConstant().getText());
    assertEquals("mat2(1.0)", BasicType.MAT2X2.getCanonicalConstant().getText());
  }

  @Test
  public void testNumericTypes() {
    assertTrue(BasicType.allNumericTypes().contains(BasicType.IVEC2));
    assertFalse(BasicType.allNumericTypes().contains(BasicType.BOOL));
    assertFalse(BasicType.allNumericTypes().contains(BasicType.BVEC2));
    assertFalse(BasicType.allNumericTypes().contains(BasicType.BVEC3));
    assertFalse(BasicType.allNumericTypes().contains(BasicType.BVEC4));
  }

}