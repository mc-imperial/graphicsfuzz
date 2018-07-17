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

package com.graphicsfuzz.common.glslversion;

final class Essl310 extends CompositeShadingLanguageVersion {

  static final ShadingLanguageVersion INSTANCE = new Essl310(Essl300.INSTANCE);

  private Essl310(ShadingLanguageVersion prototype) {
    super(prototype);
    // Singleton
  }

  @Override
  public String getVersionString() {
    return "310 es";
  }

  @Override
  public boolean supportedMixNonfloatBool() {
    return true;
  }

  @Override
  public boolean supportedPackSnorm4x8() {
    return true;
  }

  @Override
  public boolean supportedPackUnorm4x8() {
    return true;
  }

  @Override
  public boolean supportedUnpackSnorm4x8() {
    return true;
  }

  @Override
  public boolean supportedUnpackUnorm4x8() {
    return true;
  }

}
