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

package com.graphicsfuzz.common.util;

import com.graphicsfuzz.common.ast.CompareAstsDuplicate;
import com.graphicsfuzz.common.ast.TranslationUnit;
import org.junit.Test;

public class AvoidDeprecatedGlFragColorTest {

  @Test
  public void testAvoidDeprecatedFragColor() throws Exception {
    final String expectedProg = "layout(location = 0) out vec4 blargh;\n"
        + "void main() { blargh = vec4(0.0); }";
    final String prog = "void main() { "
        + OpenGlConstants.GL_FRAG_COLOR + " = vec4(0.0); }";
    final TranslationUnit tu = ParseHelper.parse(prog, false);
    AvoidDeprecatedGlFragColor.avoidDeprecatedGlFragColor(tu, "blargh");
    CompareAstsDuplicate.assertEqualAsts(expectedProg, tu);
  }

}