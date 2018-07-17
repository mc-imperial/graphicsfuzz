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

import com.graphicsfuzz.common.ast.expr.Expr;
import com.graphicsfuzz.common.ast.visitors.IAstVisitor;

public class SamplerType extends BuiltinType {

  private SamplerType() {
    // SamplerType is in essence an enumeration.
    // No need to override .equals() and .hashCode()
  }

  public static final SamplerType SAMPLER1D = new SamplerType();
  public static final SamplerType SAMPLER2D = new SamplerType();
  public static final SamplerType SAMPLER2DRECT = new SamplerType();
  public static final SamplerType SAMPLER3D = new SamplerType();
  public static final SamplerType SAMPLERCUBE = new SamplerType();
  public static final SamplerType SAMPLEREXTERNALOES = new SamplerType();
  public static final SamplerType SAMPLER1DSHADOW = new SamplerType();
  public static final SamplerType SAMPLER2DSHADOW = new SamplerType();
  public static final SamplerType SAMPLER2DRECTSHADOW = new SamplerType();
  public static final SamplerType SAMPLERCUBESHADOW = new SamplerType();
  public static final SamplerType SAMPLER1DARRAY = new SamplerType();
  public static final SamplerType SAMPLER2DARRAY = new SamplerType();
  public static final SamplerType SAMPLER1DARRAYSHADOW = new SamplerType();
  public static final SamplerType SAMPLER2DARRAYSHADOW = new SamplerType();
  public static final SamplerType SAMPLERBUFFER = new SamplerType();
  public static final SamplerType SAMPLERCUBEARRAY = new SamplerType();
  public static final SamplerType SAMPLERCUBEARRAYSHADOW = new SamplerType();
  public static final SamplerType ISAMPLER1D = new SamplerType();
  public static final SamplerType ISAMPLER2D = new SamplerType();
  public static final SamplerType ISAMPLER2DRECT = new SamplerType();
  public static final SamplerType ISAMPLER3D = new SamplerType();
  public static final SamplerType ISAMPLERCUBE = new SamplerType();
  public static final SamplerType ISAMPLER1DARRAY = new SamplerType();
  public static final SamplerType ISAMPLER2DARRAY = new SamplerType();
  public static final SamplerType ISAMPLERBUFFER = new SamplerType();
  public static final SamplerType ISAMPLERCUBEARRAY = new SamplerType();
  public static final SamplerType USAMPLER1D = new SamplerType();
  public static final SamplerType USAMPLER2D = new SamplerType();
  public static final SamplerType USAMPLER2DRECT = new SamplerType();
  public static final SamplerType USAMPLER3D = new SamplerType();
  public static final SamplerType USAMPLERCUBE = new SamplerType();
  public static final SamplerType USAMPLER1DARRAY = new SamplerType();
  public static final SamplerType USAMPLER2DARRAY = new SamplerType();
  public static final SamplerType USAMPLERBUFFER = new SamplerType();
  public static final SamplerType USAMPLERCUBEARRAY = new SamplerType();
  public static final SamplerType SAMPLER2DMS = new SamplerType();
  public static final SamplerType ISAMPLER2DMS = new SamplerType();
  public static final SamplerType USAMPLER2DMS = new SamplerType();
  public static final SamplerType SAMPLER2DMSARRAY = new SamplerType();
  public static final SamplerType ISAMPLER2DMSARRAY = new SamplerType();
  public static final SamplerType USAMPLER2DMSARRAY = new SamplerType();

  @Override
  public String toString() {
    if (this == SAMPLER1D) {
      return "sampler1D";
    }
    if (this == SAMPLER2D) {
      return "sampler2D";
    }
    if (this == SAMPLER2DRECT) {
      return "sampler2drect";
    }
    if (this == SAMPLER3D) {
      return "sampler3D";
    }
    if (this == SAMPLERCUBE) {
      return "samplercube";
    }
    if (this == SAMPLEREXTERNALOES) {
      return "samplerexternaloes";
    }
    if (this == SAMPLER1DSHADOW) {
      return "sampler1dshadow";
    }
    if (this == SAMPLER2DSHADOW) {
      return "sampler2dshadow";
    }
    if (this == SAMPLER2DRECTSHADOW) {
      return "sampler2drectshadow";
    }
    if (this == SAMPLERCUBESHADOW) {
      return "samplercubeshadow";
    }
    if (this == SAMPLER1DARRAY) {
      return "sampler1darray";
    }
    if (this == SAMPLER2DARRAY) {
      return "sampler2darray";
    }
    if (this == SAMPLER1DARRAYSHADOW) {
      return "sampler1darrayshadow";
    }
    if (this == SAMPLER2DARRAYSHADOW) {
      return "sampler2darrayshadow";
    }
    if (this == SAMPLERBUFFER) {
      return "samplerbuffer";
    }
    if (this == SAMPLERCUBEARRAY) {
      return "samplercubearray";
    }
    if (this == SAMPLERCUBEARRAYSHADOW) {
      return "samplercubearrayshadow";
    }
    if (this == ISAMPLER1D) {
      return "isampler1d";
    }
    if (this == ISAMPLER2D) {
      return "isampler2d";
    }
    if (this == ISAMPLER2DRECT) {
      return "isampler2drect";
    }
    if (this == ISAMPLER3D) {
      return "isampler3d";
    }
    if (this == ISAMPLERCUBE) {
      return "isamplercube";
    }
    if (this == ISAMPLER1DARRAY) {
      return "isampler1darray";
    }
    if (this == ISAMPLER2DARRAY) {
      return "isampler2darray";
    }
    if (this == ISAMPLERBUFFER) {
      return "isamplerbuffer";
    }
    if (this == ISAMPLERCUBEARRAY) {
      return "isamplercubearray";
    }
    if (this == USAMPLER1D) {
      return "usampler1d";
    }
    if (this == USAMPLER2D) {
      return "usampler2d";
    }
    if (this == USAMPLER2DRECT) {
      return "usampler2drect";
    }
    if (this == USAMPLER3D) {
      return "usampler3d";
    }
    if (this == USAMPLERCUBE) {
      return "usamplercube";
    }
    if (this == USAMPLER1DARRAY) {
      return "usampler1darray";
    }
    if (this == USAMPLER2DARRAY) {
      return "usampler2darray";
    }
    if (this == USAMPLERBUFFER) {
      return "usamplerbuffer";
    }
    if (this == USAMPLERCUBEARRAY) {
      return "usamplercubearray";
    }
    if (this == SAMPLER2DMS) {
      return "sampler2dms";
    }
    if (this == ISAMPLER2DMS) {
      return "isampler2dms";
    }
    if (this == USAMPLER2DMS) {
      return "usampler2dms";
    }
    if (this == SAMPLER2DMSARRAY) {
      return "sampler2dmsarray";
    }
    if (this == ISAMPLER2DMSARRAY) {
      return "isampler2dmsarray";
    }
    if (this == USAMPLER2DMSARRAY) {
      return "usampler2dmsarray";
    }
    throw new RuntimeException("Invalid type");
  }

  @Override
  public Expr getCanonicalConstant() {
    assert !hasCanonicalConstant();
    throw new RuntimeException("No canonical constant for " + this);
  }

  @Override
  public boolean hasCanonicalConstant() {
    return false;
  }

  @Override
  public void accept(IAstVisitor visitor) {
    visitor.visitSamplerType(this);
  }

}
