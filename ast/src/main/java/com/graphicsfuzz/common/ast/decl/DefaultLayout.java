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

import com.graphicsfuzz.common.ast.type.LayoutQualifier;
import com.graphicsfuzz.common.ast.type.TypeQualifier;
import com.graphicsfuzz.common.ast.visitors.IAstVisitor;
import java.util.Arrays;

public class DefaultLayout extends Declaration {

  private LayoutQualifier layoutQualifier;
  private TypeQualifier typeQualifier;

  public DefaultLayout(LayoutQualifier layoutQualifier, TypeQualifier typeQualifier) {
    assert Arrays.asList(
        TypeQualifier.UNIFORM,
        TypeQualifier.BUFFER,
        TypeQualifier.SHADER_INPUT,
        TypeQualifier.SHADER_OUTPUT).contains(typeQualifier);
    this.layoutQualifier = layoutQualifier;
    this.typeQualifier = typeQualifier;
  }

  public LayoutQualifier getLayoutQualifier() {
    return layoutQualifier;
  }

  public TypeQualifier getTypeQualifier() {
    return typeQualifier;
  }

  @Override
  public void accept(IAstVisitor visitor) {
    visitor.visitDefaultLayout(this);
  }

  @Override
  public DefaultLayout clone() {
    return new DefaultLayout(layoutQualifier, typeQualifier);
  }

}
