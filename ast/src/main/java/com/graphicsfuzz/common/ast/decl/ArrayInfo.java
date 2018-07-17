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
import java.util.Optional;

public class ArrayInfo implements IAstNode {

  private Optional<Integer> size;

  public ArrayInfo(Integer size) {
    this.size = Optional.of(size);
  }

  public ArrayInfo() {
    this.size = Optional.empty();
  }

  public boolean hasSize() {
    return size.isPresent();
  }

  public Integer getSize() {
    return size.get();
  }

  @Override
  public void accept(IAstVisitor visitor) {
    visitor.visitArrayInfo(this);
  }

  @Override
  public ArrayInfo clone() {
    return hasSize() ? new ArrayInfo(getSize()) : new ArrayInfo();
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof ArrayInfo && size.equals(((ArrayInfo) obj).size);
  }

  @Override
  public int hashCode() {
    return size.hashCode();
  }

}
