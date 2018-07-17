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

public class TypeQualifier {

  private final String text;

  /**
   * Deliverately package-visible: we do not want arbitrary type qualifiers
   * to be created, except via designated subclasses.
   */
  TypeQualifier(String text) {
    // Do not permit external creation
    this.text = text;
  }

  public static final TypeQualifier INVARIANT = new TypeQualifier("invariant");
  public static final TypeQualifier PRECISE = new TypeQualifier("precise");
  public static final TypeQualifier CENTROID = new TypeQualifier("centroid");
  public static final TypeQualifier SAMPLE = new TypeQualifier("sample");
  public static final TypeQualifier CONST = new TypeQualifier("const");
  public static final TypeQualifier ATTRIBUTE = new TypeQualifier("attribute");
  public static final TypeQualifier VARYING = new TypeQualifier("varying");
  public static final TypeQualifier IN_PARAM = new TypeQualifier("in");
  public static final TypeQualifier OUT_PARAM = new TypeQualifier("out");
  public static final TypeQualifier INOUT_PARAM = new TypeQualifier("inout");
  public static final TypeQualifier UNIFORM = new TypeQualifier("uniform");
  public static final TypeQualifier COHERENT = new TypeQualifier("coherent");
  public static final TypeQualifier VOLATILE = new TypeQualifier("volatile");
  public static final TypeQualifier RESTRICT = new TypeQualifier("restrict");
  public static final TypeQualifier READONLY = new TypeQualifier("readonly");
  public static final TypeQualifier WRITEONLY = new TypeQualifier("writeonly");
  public static final TypeQualifier FLAT = new TypeQualifier("flat");
  public static final TypeQualifier SMOOTH = new TypeQualifier("smooth");
  public static final TypeQualifier NOPERSPECTIVE = new TypeQualifier("noperspective");
  public static final TypeQualifier HIGHP = new TypeQualifier("highp");
  public static final TypeQualifier MEDIUMP = new TypeQualifier("mediump");
  public static final TypeQualifier LOWP = new TypeQualifier("lowp");
  public static final TypeQualifier SHADER_INPUT = new TypeQualifier("in");
  public static final TypeQualifier SHADER_OUTPUT = new TypeQualifier("out");
  public static final TypeQualifier BUFFER = new TypeQualifier("buffer");

  @Override
  public String toString() {
    return text;
  }

}