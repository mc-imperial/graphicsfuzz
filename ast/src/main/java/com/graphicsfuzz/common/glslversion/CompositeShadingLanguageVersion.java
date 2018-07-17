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

abstract class CompositeShadingLanguageVersion implements ShadingLanguageVersion {

  private final ShadingLanguageVersion prototype;

  CompositeShadingLanguageVersion(
      ShadingLanguageVersion prototype) {
    this.prototype = prototype;
  }

  @Override
  public abstract String getVersionString();

  @Override
  public boolean globalVariableInitializersMustBeConst() {
    return prototype.globalVariableInitializersMustBeConst();
  }

  @Override
  public boolean initializersOfConstMustBeConst() {
    return prototype.initializersOfConstMustBeConst();
  }

  @Override
  public boolean isWebGl() {
    return prototype.isWebGl();
  }

  @Override
  public boolean restrictedArrayIndexing() {
    return prototype.restrictedArrayIndexing();
  }

  @Override
  public boolean restrictedForLoops() {
    return prototype.restrictedForLoops();
  }

  @Override
  public boolean supportedAbsInt() {
    return prototype.supportedAbsInt();
  }

  @Override
  public boolean supportedBitwiseOperations() {
    return prototype.supportedBitwiseOperations();
  }

  @Override
  public boolean supportedClampInt() {
    return prototype.supportedClampInt();
  }

  @Override
  public boolean supportedClampUint() {
    return prototype.supportedClampUint();
  }

  @Override
  public boolean supportedDeterminant() {
    return prototype.supportedDeterminant();
  }

  @Override
  public boolean supportedDoStmt() {
    return prototype.supportedDoStmt();
  }

  @Override
  public boolean supportedFloatBitsToInt() {
    return prototype.supportedFloatBitsToInt();
  }

  @Override
  public boolean supportedFloatBitsToUint() {
    return prototype.supportedFloatBitsToUint();
  }

  @Override
  public boolean supportedFma() {
    return prototype.supportedFma();
  }

  @Override
  public boolean supportedGlFragColor() {
    return prototype.supportedGlFragColor();
  }

  @Override
  public boolean supportedIntBitsToFloat() {
    return prototype.supportedIntBitsToFloat();
  }

  @Override
  public boolean supportedInverse() {
    return prototype.supportedInverse();
  }

  @Override
  public boolean supportedIsinf() {
    return prototype.supportedIsinf();
  }

  @Override
  public boolean supportedIsnan() {
    return prototype.supportedIsnan();
  }

  @Override
  public boolean supportedMatrixCompMultNonSquare() {
    return prototype.supportedMatrixCompMultNonSquare();
  }

  @Override
  public boolean supportedMaxInt() {
    return prototype.supportedMaxInt();
  }

  @Override
  public boolean supportedMaxUint() {
    return prototype.supportedMaxUint();
  }

  @Override
  public boolean supportedMinInt() {
    return prototype.supportedMinInt();
  }

  @Override
  public boolean supportedMinUint() {
    return prototype.supportedMinUint();
  }

  @Override
  public boolean supportedMixFloatBool() {
    return prototype.supportedMixFloatBool();
  }

  @Override
  public boolean supportedMixNonfloatBool() {
    return prototype.supportedMixNonfloatBool();
  }

  @Override
  public boolean supportedNonSquareMatrices() {
    return prototype.supportedNonSquareMatrices();
  }

  @Override
  public boolean supportedOuterProduct() {
    return prototype.supportedOuterProduct();
  }

  @Override
  public boolean supportedPackHalf2x16() {
    return prototype.supportedPackHalf2x16();
  }

  @Override
  public boolean supportedPackSnorm2x16() {
    return prototype.supportedPackSnorm2x16();
  }

  @Override
  public boolean supportedPackSnorm4x8() {
    return prototype.supportedPackSnorm4x8();
  }

  @Override
  public boolean supportedPackUnorm2x16() {
    return prototype.supportedPackUnorm2x16();
  }

  @Override
  public boolean supportedPackUnorm4x8() {
    return prototype.supportedPackUnorm4x8();
  }

  @Override
  public boolean supportedSignInt() {
    return prototype.supportedSignInt();
  }

  @Override
  public boolean supportedRound() {
    return prototype.supportedRound();
  }

  @Override
  public boolean supportedRoundEven() {
    return prototype.supportedRoundEven();
  }

  @Override
  public boolean supportedSwitchStmt() {
    return prototype.supportedSwitchStmt();
  }

  @Override
  public boolean supportedTranspose() {
    return prototype.supportedTranspose();
  }

  @Override
  public boolean supportedTrunc() {
    return prototype.supportedTrunc();
  }

  @Override
  public boolean supportedUintBitsToFloat() {
    return prototype.supportedUintBitsToFloat();
  }

  @Override
  public boolean supportedUnpackHalf2x16() {
    return prototype.supportedUnpackHalf2x16();
  }

  @Override
  public boolean supportedUnpackSnorm2x16() {
    return prototype.supportedUnpackSnorm2x16();
  }

  @Override
  public boolean supportedUnpackSnorm4x8() {
    return prototype.supportedUnpackSnorm4x8();
  }

  @Override
  public boolean supportedUnpackUnorm2x16() {
    return prototype.supportedUnpackUnorm2x16();
  }

  @Override
  public boolean supportedUnpackUnorm4x8() {
    return prototype.supportedUnpackUnorm4x8();
  }

  @Override
  public boolean supportedUnsigned() {
    return prototype.supportedUnsigned();
  }

}
