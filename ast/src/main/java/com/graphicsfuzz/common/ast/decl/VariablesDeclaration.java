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

import com.graphicsfuzz.common.ast.type.Type;
import com.graphicsfuzz.common.ast.visitors.IAstVisitor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class VariablesDeclaration extends Declaration {

  private Type baseType;
  private List<VariableDeclInfo> declInfos;

  public VariablesDeclaration(Type baseType, List<VariableDeclInfo> declInfo) {
    assert baseType != null;
    this.baseType = baseType;
    this.declInfos = declInfo;
  }

  public VariablesDeclaration(Type baseType, VariableDeclInfo decl) {
    this(baseType, new ArrayList<>());
    declInfos.add(decl);
  }

  public Type getBaseType() {
    return baseType;
  }

  public void setBaseType(Type baseType) {
    this.baseType = baseType;
  }

  public VariableDeclInfo getDeclInfo(int index) {
    return declInfos.get(index);
  }

  public List<VariableDeclInfo> getDeclInfos() {
    return Collections.unmodifiableList(declInfos);
  }

  public void setDeclInfo(int index, VariableDeclInfo variableDeclInfo) {
    declInfos.set(index, variableDeclInfo);
  }

  public int getNumDecls() {
    return declInfos.size();
  }

  public void removeDeclInfo(int index) {
    declInfos.remove(index);
  }

  @Override
  public void accept(IAstVisitor visitor) {
    visitor.visitVariablesDeclaration(this);
  }

  @Override
  public VariablesDeclaration clone() {
    return new VariablesDeclaration(baseType.clone(),
        declInfos.stream().map(x -> x.clone()).collect(Collectors.toList()));
  }

}
