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

package com.graphicsfuzz.common.ast.visitors;

/**
 * Class to represent visitation depth, which is right now just an integer.  Using a class to
 * (a) make sure we do pass what is specifically a visitation depth, not some other integer;
 * (b) future-proof so that we can enrich the notion of depth (e.g., to include details of
 * parents in the AST) if needed.
 */
public class VisitationDepth implements Comparable<VisitationDepth> {

  private final Integer depth;

  public VisitationDepth(int depth) {
    this.depth = depth;
  }

  public VisitationDepth deeper() {
    return new VisitationDepth(depth + 1);
  }

  @Override
  public int compareTo(VisitationDepth other) {
    return depth.compareTo(other.depth);
  }
}
