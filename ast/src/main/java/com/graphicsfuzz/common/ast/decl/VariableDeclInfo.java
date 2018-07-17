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

import com.graphicsfuzz.common.ast.IAstNode;
import com.graphicsfuzz.common.ast.visitors.IAstVisitor;

public class VariableDeclInfo implements IAstNode {

  private String name;
  private ArrayInfo arrayInfo;
  private Initializer initializer;

  public VariableDeclInfo(String name, ArrayInfo arrayInfo, Initializer initializer) {
    this.name = name;
    this.arrayInfo = arrayInfo;
    this.initializer = initializer;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public ArrayInfo getArrayInfo() {
    return arrayInfo;
  }

  public Initializer getInitializer() {
    return initializer;
  }

  public boolean hasArrayInfo() {
    return getArrayInfo() != null;
  }

  public boolean hasInitializer() {
    return getInitializer() != null;
  }

  public void setInitializer(Initializer initializer) {
    this.initializer = initializer;
  }

  @Override
  public void accept(IAstVisitor visitor) {
    visitor.visitVariableDeclInfo(this);
  }

  @Override
  public VariableDeclInfo clone() {
    return new VariableDeclInfo(name, arrayInfo == null ? null : arrayInfo.clone(),
        initializer == null ? null : initializer.clone());
  }

}
