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

final class Essl100 implements ShadingLanguageVersion {

  static final ShadingLanguageVersion INSTANCE = new Essl100();

  private Essl100() {
    // Singleton
  }

  @Override
  public String getVersionString() {
    return "100";
  }

  @Override
  public boolean globalVariableInitializersMustBeConst() {
    return true;
  }

  @Override
  public boolean initializersOfConstMustBeConst() {
    return true;
  }

  @Override
  public boolean isWebGl() {
    return false;
  }

  @Override
  public boolean restrictedArrayIndexing() {
    return true;
  }

  @Override
  public boolean restrictedForLoops() {
    return true;
  }

  @Override
  public boolean supportedAbsInt() {
    return false;
  }

  @Override
  public boolean supportedBitwiseOperations() {
    return false;
  }

  @Override
  public boolean supportedClampInt() {
    return false;
  }

  @Override
  public boolean supportedClampUint() {
    return false;
  }

  @Override
  public boolean supportedDeterminant() {
    return false;
  }

  @Override
  public boolean supportedDoStmt() {
    return true;
  }

  @Override
  public boolean supportedFloatBitsToInt() {
    return false;
  }

  @Override
  public boolean supportedFloatBitsToUint() {
    return false;
  }

  @Override
  public boolean supportedFma() {
    return false;
  }

  @Override
  public boolean supportedGlFragColor() {
    return true;
  }

  @Override
  public boolean supportedIntBitsToFloat() {
    return false;
  }

  @Override
  public boolean supportedInverse() {
    return false;
  }

  @Override
  public boolean supportedIsinf() {
    return false;
  }

  @Override
  public boolean supportedIsnan() {
    return false;
  }

  @Override
  public boolean supportedMatrixCompMultNonSquare() {
    return false;
  }

  @Override
  public boolean supportedMaxInt() {
    return false;
  }

  @Override
  public boolean supportedMaxUint() {
    return false;
  }

  @Override
  public boolean supportedMinInt() {
    return false;
  }

  @Override
  public boolean supportedMinUint() {
    return false;
  }

  @Override
  public boolean supportedMixFloatBool() {
    return false;
  }

  @Override
  public boolean supportedMixNonfloatBool() {
    return false;
  }

  @Override
  public boolean supportedNonSquareMatrices() {
    return false;
  }

  @Override
  public boolean supportedOuterProduct() {
    return false;
  }

  @Override
  public boolean supportedPackHalf2x16() {
    return false;
  }

  @Override
  public boolean supportedPackSnorm2x16() {
    return false;
  }

  @Override
  public boolean supportedPackSnorm4x8() {
    return false;
  }

  @Override
  public boolean supportedPackUnorm2x16() {
    return false;
  }

  @Override
  public boolean supportedPackUnorm4x8() {
    return false;
  }

  @Override
  public boolean supportedRound() {
    return false;
  }

  @Override
  public boolean supportedRoundEven() {
    return false;
  }

  @Override
  public boolean supportedSignInt() {
    return false;
  }

  @Override
  public boolean supportedSwitchStmt() {
    return false;
  }

  @Override
  public boolean supportedTranspose() {
    return false;
  }

  @Override
  public boolean supportedTrunc() {
    return false;
  }

  @Override
  public boolean supportedUintBitsToFloat() {
    return false;
  }

  @Override
  public boolean supportedUnpackHalf2x16() {
    return false;
  }

  @Override
  public boolean supportedUnpackSnorm2x16() {
    return false;
  }

  @Override
  public boolean supportedUnpackSnorm4x8() {
    return false;
  }

  @Override
  public boolean supportedUnpackUnorm2x16() {
    return false;
  }

  @Override
  public boolean supportedUnpackUnorm4x8() {
    return false;
  }

  @Override
  public boolean supportedUnsigned() {
    return false;
  }
}
