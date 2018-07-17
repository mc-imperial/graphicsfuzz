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

package com.graphicsfuzz.astfuzzer.tool;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.graphicsfuzz.common.ast.type.BasicType;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.Test;

public class TestFunctionSignature {

  @Test
  public void testMatchForEqualSignatures() {

    Signature first = new Signature(BasicType.FLOAT,
        new ArrayList<>(Arrays.asList(BasicType.BVEC2, BasicType.IVEC3)));
    Signature second = new Signature(BasicType.FLOAT,
        new ArrayList<>(Arrays.asList(BasicType.BVEC2, BasicType.IVEC3)));
    assertTrue(first.matches(second));
    assertTrue(second.matches(first));
  }

  @Test
  public void testMatchesForEqualOrNull() {

    Signature first = new Signature(null,
        new ArrayList<>(Arrays.asList(BasicType.BVEC2, BasicType.IVEC3)));
    Signature second = new Signature(BasicType.FLOAT,
        new ArrayList<>(Arrays.asList(BasicType.BVEC2, null)));
    assertTrue(first.matches(second));
    assertTrue(second.matches(first));
  }

  @Test
  public void testMatchesForDifferentSignatures() {

    Signature first = new Signature(null,
        new ArrayList<>(Arrays.asList(BasicType.BVEC4, BasicType.IVEC3)));
    Signature second = new Signature(BasicType.FLOAT,
        new ArrayList<>(Arrays.asList(BasicType.BVEC2, null)));
    assertFalse(first.matches(second));
    assertFalse(second.matches(first));
  }


}
