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

import com.graphicsfuzz.common.ast.type.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Signature {

  private final List<Type> parameterTypes;
  private final Type returnType;

  /**
   * Constructor.
   *
   * @param returnType ReturnType of the function.
   * @param args List containing the argument types.
   */
  public Signature(Type returnType, List<Type> args) {
    this.returnType = returnType;
    this.parameterTypes = new ArrayList<>();
    this.parameterTypes.addAll(args);
  }

  public Signature(Type returnType, Type... args) {
    this.returnType = returnType;
    this.parameterTypes = new ArrayList<>();
    this.parameterTypes.addAll(Arrays.asList(args));
  }

  /**
   * Similar to equals, but in this case null is considered to be equal to anything.
   *
   * @param thatSignature Signature to compare with.
   * @return True if the functionSignatures match, false otherwise.
   */
  boolean matches(Signature thatSignature) {

    if (!equalOrNull(returnType, thatSignature.returnType)) {
      return false;
    }

    if (parameterTypes.size() != thatSignature.parameterTypes.size()) {
      return false;
    }

    for (int i = 0; i < parameterTypes.size(); i++) {
      if (!equalOrNull(parameterTypes.get(i),
          thatSignature.parameterTypes.get(i))) {
        return false;
      }
    }
    return true;
  }

  private boolean equalOrNull(Type thisType, Type thatType) {

    if (thisType == null || thatType == null
        || thisType.getWithoutQualifiers().equals(thatType.getWithoutQualifiers())) {
      return true;
    }
    return false;
  }

}