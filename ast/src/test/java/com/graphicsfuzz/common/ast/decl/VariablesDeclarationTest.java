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

package com.graphicsfuzz.common.ast.decl;

import static org.junit.Assert.*;

import com.graphicsfuzz.common.ast.CompareAstsDuplicate;
import com.graphicsfuzz.common.ast.TranslationUnit;
import com.graphicsfuzz.common.ast.visitors.StandardVisitor;
import com.graphicsfuzz.common.util.ParseHelper;
import org.junit.Test;

public class VariablesDeclarationTest {

  @Test
  public void testSetDeclInfo() throws Exception {
    final String program = "int x, y; int main() { int y, z, w; }";
    final TranslationUnit tu = ParseHelper.parse(program, false);
    new StandardVisitor() {
      @Override
      public void visitVariablesDeclaration(VariablesDeclaration variablesDeclaration) {
        super.visitVariablesDeclaration(variablesDeclaration);
        for (int i = 0; i < variablesDeclaration.getNumDecls(); i++) {
          final VariableDeclInfo vdi = variablesDeclaration.getDeclInfo(i);
          variablesDeclaration.setDeclInfo(i, new VariableDeclInfo(vdi.getName() + "_modified",
              vdi.getArrayInfo(), vdi.getInitializer()));
        }
      }
    }.visit(tu);
    CompareAstsDuplicate.assertEqualAsts(
        "int x_modified, y_modified; int main() { int y_modified, z_modified, w_modified; }",
        tu);
  }


}