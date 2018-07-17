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

public class ImageType extends BuiltinType {

  private ImageType() {
    // ImageType is in essence an enumeration.
    // No need to override .equals() and .hashCode()
  }

  public static final ImageType IMAGE1D = new ImageType();
  public static final ImageType IMAGE2D = new ImageType();
  public static final ImageType IMAGE3D = new ImageType();
  public static final ImageType IMAGE2DRECT = new ImageType();
  public static final ImageType IMAGECUBE = new ImageType();
  public static final ImageType IMAGEBUFFER = new ImageType();
  public static final ImageType IMAGE1DARRAY = new ImageType();
  public static final ImageType IMAGE2DARRAY = new ImageType();
  public static final ImageType IMAGECUBEARRAY = new ImageType();
  public static final ImageType IMAGE2DMS = new ImageType();
  public static final ImageType IMAGE2DMSARRAY = new ImageType();
  public static final ImageType IIMAGE1D = new ImageType();
  public static final ImageType IIMAGE2D = new ImageType();
  public static final ImageType IIMAGE3D = new ImageType();
  public static final ImageType IIMAGE2DRECT = new ImageType();
  public static final ImageType IIMAGECUBE = new ImageType();
  public static final ImageType IIMAGEBUFFER = new ImageType();
  public static final ImageType IIMAGE1DARRAY = new ImageType();
  public static final ImageType IIMAGE2DARRAY = new ImageType();
  public static final ImageType IIMAGECUBEARRAY = new ImageType();
  public static final ImageType IIMAGE2DMS = new ImageType();
  public static final ImageType IIMAGE2DMSARRAY = new ImageType();
  public static final ImageType UIMAGE1D = new ImageType();
  public static final ImageType UIMAGE2D = new ImageType();
  public static final ImageType UIMAGE3D = new ImageType();
  public static final ImageType UIMAGE2DRECT = new ImageType();
  public static final ImageType UIMAGECUBE = new ImageType();
  public static final ImageType UIMAGEBUFFER = new ImageType();
  public static final ImageType UIMAGE1DARRAY = new ImageType();
  public static final ImageType UIMAGE2DARRAY = new ImageType();
  public static final ImageType UIMAGECUBEARRAY = new ImageType();
  public static final ImageType UIMAGE2DMS = new ImageType();
  public static final ImageType UIMAGE2DMSARRAY = new ImageType();

  @Override
  public String toString() {
    if (this == IMAGE1D) {
      return "image1d";
    }
    if (this == IMAGE2D) {
      return "image2d";
    }
    if (this == IMAGE3D) {
      return "image3d";
    }
    if (this == IMAGE2DRECT) {
      return "image2drect";
    }
    if (this == IMAGECUBE) {
      return "imagecube";
    }
    if (this == IMAGEBUFFER) {
      return "imagebuffer";
    }
    if (this == IMAGE1DARRAY) {
      return "image1darray";
    }
    if (this == IMAGE2DARRAY) {
      return "image2darray";
    }
    if (this == IMAGECUBEARRAY) {
      return "imagecubearray";
    }
    if (this == IMAGE2DMS) {
      return "image2dms";
    }
    if (this == IMAGE2DMSARRAY) {
      return "image2dmsarray";
    }
    if (this == IIMAGE1D) {
      return "iimage1d";
    }
    if (this == IIMAGE2D) {
      return "iimage2d";
    }
    if (this == IIMAGE3D) {
      return "iimage3d";
    }
    if (this == IIMAGE2DRECT) {
      return "iimage2drect";
    }
    if (this == IIMAGECUBE) {
      return "iimagecube";
    }
    if (this == IIMAGEBUFFER) {
      return "iimagebuffer";
    }
    if (this == IIMAGE1DARRAY) {
      return "iimage1darray";
    }
    if (this == IIMAGE2DARRAY) {
      return "iimage2darray";
    }
    if (this == IIMAGECUBEARRAY) {
      return "iimagecubearray";
    }
    if (this == IIMAGE2DMS) {
      return "iimage2dms";
    }
    if (this == IIMAGE2DMSARRAY) {
      return "iimage2dmsarray";
    }
    if (this == UIMAGE1D) {
      return "uimage1d";
    }
    if (this == UIMAGE2D) {
      return "uimage2d";
    }
    if (this == UIMAGE3D) {
      return "uimage3d";
    }
    if (this == UIMAGE2DRECT) {
      return "uimage2drect";
    }
    if (this == UIMAGECUBE) {
      return "uimagecube";
    }
    if (this == UIMAGEBUFFER) {
      return "uimagebuffer";
    }
    if (this == UIMAGE1DARRAY) {
      return "uimage1darray";
    }
    if (this == UIMAGE2DARRAY) {
      return "uimage2darray";
    }
    if (this == UIMAGECUBEARRAY) {
      return "uimagecubearray";
    }
    if (this == UIMAGE2DMS) {
      return "uimage2dms";
    }
    if (this == UIMAGE2DMSARRAY) {
      return "uimage2dmsarray";
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
    visitor.visitImageType(this);
  }

}
