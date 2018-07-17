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

import com.graphicsfuzz.common.ast.decl.ArrayInfo;
import com.graphicsfuzz.common.ast.expr.Expr;
import com.graphicsfuzz.common.ast.visitors.IAstVisitor;

public class ArrayType extends Type {

  private Type baseType;
  private ArrayInfo arrayInfo;

  public ArrayType(Type baseType, ArrayInfo arrayInfo) {
    this.baseType = baseType;
    this.arrayInfo = arrayInfo;
  }

  public Type getBaseType() {
    return baseType;
  }

  public ArrayInfo getArrayInfo() {
    return arrayInfo;
  }

  @Override
  public void accept(IAstVisitor visitor) {
    visitor.visitArrayType(this);
  }

  @Override
  public boolean equals(Object that) {
    if (this == that) {
      return true;
    }
    if (!(that instanceof ArrayType)) {
      return false;
    }
    ArrayType thatArrayType = (ArrayType) that;
    return this.baseType.equals(thatArrayType.baseType)
        && this.arrayInfo.equals(thatArrayType.arrayInfo);
  }

  @Override
  public int hashCode() {
    // TODO: revisit if we end up storing large sets of types
    return baseType.hashCode() + arrayInfo.hashCode();
  }

  @Override
  public ArrayType clone() {
    return new ArrayType(baseType.clone(), arrayInfo.clone());
  }

  @Override
  public boolean hasCanonicalConstant() {
    return false;
  }

  @Override
  public Expr getCanonicalConstant() {
    throw new RuntimeException("No canonical constant for ArrayType");
  }

  @Override
  public Type getWithoutQualifiers() {
    return this;
  }

  @Override
  public boolean hasQualifier(TypeQualifier qualifier) {
    return false;
  }

}
