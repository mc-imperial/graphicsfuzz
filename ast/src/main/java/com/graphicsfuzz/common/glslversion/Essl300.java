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

final class Essl300 extends CompositeShadingLanguageVersion {

  static final ShadingLanguageVersion INSTANCE = new Essl300(Essl100.INSTANCE);

  private Essl300(ShadingLanguageVersion prototype) {
    super(prototype);
    // Singleton
  }

  @Override
  public String getVersionString() {
    return "300 es";
  }

  @Override
  public boolean restrictedForLoops() {
    return false;
  }

  @Override
  public boolean supportedAbsInt() {
    return true;
  }

  @Override
  public boolean supportedBitwiseOperations() {
    return true;
  }

  @Override
  public boolean supportedClampInt() {
    return true;
  }

  @Override
  public boolean supportedClampUint() {
    return true;
  }

  @Override
  public boolean supportedDeterminant() {
    return true;
  }

  @Override
  public boolean supportedDoStmt() {
    return true;
  }

  @Override
  public boolean supportedFloatBitsToInt() {
    return true;
  }

  @Override
  public boolean supportedFloatBitsToUint() {
    return true;
  }

  @Override
  public boolean supportedGlFragColor() {
    return false;
  }

  @Override
  public boolean supportedIntBitsToFloat() {
    return true;
  }

  @Override
  public boolean supportedInverse() {
    return true;
  }

  @Override
  public boolean supportedIsinf() {
    return true;
  }

  @Override
  public boolean supportedIsnan() {
    return true;
  }

  @Override
  public boolean supportedMatrixCompMultNonSquare() {
    return true;
  }

  @Override
  public boolean supportedMaxInt() {
    return true;
  }

  @Override
  public boolean supportedMaxUint() {
    return true;
  }

  @Override
  public boolean supportedMinInt() {
    return true;
  }

  @Override
  public boolean supportedMinUint() {
    return true;
  }

  @Override
  public boolean supportedMixFloatBool() {
    return true;
  }

  @Override
  public boolean supportedNonSquareMatrices() {
    return true;
  }

  @Override
  public boolean supportedOuterProduct() {
    return true;
  }

  @Override
  public boolean supportedPackHalf2x16() {
    return true;
  }

  @Override
  public boolean supportedPackSnorm2x16() {
    return true;
  }

  @Override
  public boolean supportedPackUnorm2x16() {
    return true;
  }

  @Override
  public boolean supportedRound() {
    return true;
  }

  @Override
  public boolean supportedRoundEven() {
    return true;
  }

  @Override
  public boolean supportedSwitchStmt() {
    return true;
  }

  @Override
  public boolean supportedSignInt() {
    return true;
  }

  @Override
  public boolean supportedTranspose() {
    return true;
  }

  @Override
  public boolean supportedTrunc() {
    return true;
  }

  @Override
  public boolean supportedUintBitsToFloat() {
    return true;
  }

  @Override
  public boolean supportedUnpackHalf2x16() {
    return true;
  }

  @Override
  public boolean supportedUnpackSnorm2x16() {
    return true;
  }

  @Override
  public boolean supportedUnpackUnorm2x16() {
    return true;
  }

  @Override
  public boolean supportedUnsigned() {
    return true;
  }

}
