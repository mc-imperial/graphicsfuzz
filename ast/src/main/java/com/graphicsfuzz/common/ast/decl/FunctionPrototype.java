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

public class FunctionPrototype extends Declaration {

  private String name;
  private Type returnType;
  private List<ParameterDecl> parameters;

  public FunctionPrototype(String name, Type returnType, List<ParameterDecl> parameters) {
    assert name != null;
    assert returnType != null;
    assert parameters != null;
    this.name = name;
    this.returnType = returnType;
    this.parameters = parameters;
  }

  public FunctionPrototype(String name, Type returnType, Type... args) {
    this.name = name;
    this.returnType = returnType;
    this.parameters = new ArrayList<>();
    for (int i = 0; i < args.length; i++) {
      this.parameters.add(new ParameterDecl("p" + i, args[i], null));
    }
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Type getReturnType() {
    return returnType;
  }

  public List<ParameterDecl> getParameters() {
    return Collections.unmodifiableList(parameters);
  }

  public int getNumParameters() {
    return parameters.size();
  }

  public ParameterDecl getParameter(int index) {
    return parameters.get(index);
  }

  /**
   * Return true if and only if this prototype and the given prototype have identical names,
   * return types, and argument types.
   *
   * @param functionPrototype The function prototype to be checked
   * @return true if and only if the prototypes match
   */
  public boolean matches(FunctionPrototype functionPrototype) {
    if (!getName().equals(functionPrototype.getName())) {
      return false;
    }
    if (getNumParameters() != functionPrototype.getNumParameters()) {
      return false;
    }
    if (!getReturnType().equals(functionPrototype.getReturnType())) {
      return false;
    }

    for (int i = 0; i < getNumParameters(); i++) {
      if (!getParameters().get(i).getType().equals(functionPrototype.getParameters()
          .get(i).getType())) {
        return false;
      }
    }
    return true;
  }

  /**
   * Return true if and only if this prototype and the given prototype match exactly,
   * except that they differ in return type or in type qualifiers of parameters.
   * Thus they are too similar to be overloads of one another, yet do not match
   * perfectly.
   *
   * @param functionPrototype The function prototype to be checked
   * @return true if and only if the prototypes clash
   */
  public boolean clashes(FunctionPrototype functionPrototype) {
    if (matches(functionPrototype)) {
      return false;
    }
    if (!getName().equals(functionPrototype.getName())) {
      return false;
    }
    if (getNumParameters() != functionPrototype.getNumParameters()) {
      return false;
    }
    for (int i = 0; i < getNumParameters(); i++) {
      // If parameters differ after qualifier removal then there is no clash
      if (!getParameters().get(i).getType().getWithoutQualifiers().equals(
          functionPrototype.getParameters().get(i).getType().getWithoutQualifiers())) {
        return false;
      }
    }
    // We have two functions that do not match, yet have the same names and identical
    // parameter types once qualifiers are ignored.  This is a clash.
    return true;
  }

  @Override
  public void accept(IAstVisitor visitor) {
    visitor.visitFunctionPrototype(this);
  }

  @Override
  public FunctionPrototype clone() {
    return new FunctionPrototype(name, returnType.clone(),
        parameters.stream().map(x -> x.clone()).collect(Collectors.toList()));
  }

}
