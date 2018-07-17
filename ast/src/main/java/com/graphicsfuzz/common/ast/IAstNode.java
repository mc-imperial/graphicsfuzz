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

package com.graphicsfuzz.common.ast;

import com.graphicsfuzz.common.ast.visitors.IAstVisitor;
import com.graphicsfuzz.common.tool.PrettyPrinterVisitor;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

public interface IAstNode extends Cloneable {

  void accept(IAstVisitor visitor);

  IAstNode clone();

  /**
   * Replaces a child of the node with a new child.
   * Only available on selected classes where this has proven to be a sensible operation to perform;
   * throws an exception by default.
   * Developers should add to further classes as and when appropriate.
   *
   * @param child The child to be replaced
   * @param newChild The replacement child
   * @throws ChildDoesNotExistException if there is no such child.
   * @throws UnsupportedOperationException if the operation is not available, or if
   *                                       the new child is not suitable.
   */
  default void replaceChild(IAstNode child, IAstNode newChild) {
    throw new UnsupportedOperationException(
        "Child replacement not supported for nodes of type " + this.getClass());
  }


  /**
   * Checks whether the node has a particular child.
   * Only available on selected classes where this has proven to be a sensible operation to perform;
   * throws an exception by default.
   * Developers should add to further classes as and when appropriate.
   *
   * @param candidateChild The potential child node.
   * @throws UnsupportedOperationException if the operation is not supported.
   */
  default boolean hasChild(IAstNode candidateChild) {
    throw new UnsupportedOperationException(
          "Child querying not supported for nodes of type " + this.getClass());
  }


  /**
   * Uses the pretty printer to turn a node into a text representation.
   * @return Text representation of a node
   */
  default String getText() {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream stream = new PrintStream(baos);
    try {
      new PrettyPrinterVisitor(stream).visit(this);
      return baos.toString("UTF8");
    } catch (UnsupportedEncodingException exception) {
      return "<unknown>";
    } finally {
      stream.close();
    }
  }

}
