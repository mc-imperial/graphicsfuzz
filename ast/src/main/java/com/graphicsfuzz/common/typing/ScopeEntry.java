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

package com.graphicsfuzz.common.typing;

import com.graphicsfuzz.common.ast.decl.ParameterDecl;
import com.graphicsfuzz.common.ast.decl.VariableDeclInfo;
import com.graphicsfuzz.common.ast.decl.VariablesDeclaration;
import com.graphicsfuzz.common.ast.type.Type;
import java.util.Optional;

public class ScopeEntry {

  private final Optional<ParameterDecl> parameterDecl;

  private final Type type;

  // Represents the VariableDeclInfo that this variable came from, if one exists.  If there is no
  // such object (e.g. because the variable came from a parameter, or was made up for purposes of
  // fuzzing, or some such, the optional is empty.
  private final Optional<VariableDeclInfo> variableDeclInfo;

  // Represents the list of variable declarations that this variable came from, if one exists.
  // If there is no such object, the optional is empty.
  private final Optional<VariablesDeclaration> variablesDeclaration;

  private ScopeEntry(Type type, Optional<ParameterDecl> parameterDecl,
        Optional<VariableDeclInfo> variableDeclInfo,
        Optional<VariablesDeclaration> variablesDecl) {
    this.type = type;
    this.parameterDecl = parameterDecl;
    this.variableDeclInfo = variableDeclInfo;
    this.variablesDeclaration = variablesDecl;
  }

  public ScopeEntry(Type type,
      Optional<ParameterDecl> parameterDecl,
      VariableDeclInfo variableDeclInfo,
      VariablesDeclaration variablesDecl) {
    this(type, parameterDecl, Optional.of(variableDeclInfo), Optional.of(variablesDecl));
    assert variableDeclInfo != null;
    assert variablesDecl != null;
  }

  public ScopeEntry(Type type, Optional<ParameterDecl> parameterDecl) {
    this(type, parameterDecl, Optional.empty(), Optional.empty());
  }

  public Type getType() {
    return type;
  }

  public VariableDeclInfo getVariableDeclInfo() {
    return variableDeclInfo.get();
  }

  public boolean hasVariableDeclInfo() {
    return variableDeclInfo.isPresent();
  }

  public VariablesDeclaration getVariablesDeclaration() {
    return variablesDeclaration.get();
  }

  public boolean hasVariablesDeclaration() {
    return variablesDeclaration.isPresent();
  }

  public ParameterDecl getParameterDecl() {
    return parameterDecl.get();
  }

  public boolean hasParameterDecl() {
    return parameterDecl.isPresent();
  }

}
