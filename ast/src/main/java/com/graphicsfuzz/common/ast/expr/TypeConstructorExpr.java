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

package com.graphicsfuzz.common.ast.expr;

import com.graphicsfuzz.common.ast.IAstNode;
import com.graphicsfuzz.common.ast.visitors.IAstVisitor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TypeConstructorExpr extends Expr {

  private String type;
  private List<Expr> args;

  /**
   * Creates a type constructor expression for the given named type, with the given arguments.
   *
   * @param type Name of the type to be constructed
   * @param args Types of the arguments
   */
  public TypeConstructorExpr(String type, List<Expr> args) {
    assert type != null;
    this.type = type;
    this.args = new ArrayList<>();
    this.args.addAll(args);
  }

  public TypeConstructorExpr(String type, Expr... params) {
    this(type, Arrays.asList(params));
  }

  public String getTypename() {
    return type;
  }

  public void setTypename(String type) {
    this.type = type;
  }

  public List<Expr> getArgs() {
    return Collections.unmodifiableList(args);
  }

  public Expr getArg(int index) {
    return args.get(index);
  }

  /**
   * Removes the argument at the given index and returns it.
   *
   * @param index The index at which an argument should be removed
   * @return The removed argument
   */
  public Expr removeArg(int index) {
    return args.remove(index);
  }

  /**
   * Reveals how many arguments there are.
   *
   * @return Number of arguments
   */
  public int getNumArgs() {
    return args.size();
  }

  @Override
  public void accept(IAstVisitor visitor) {
    visitor.visitTypeConstructorExpr(this);
  }

  @Override
  public TypeConstructorExpr clone() {
    return new TypeConstructorExpr(type,
        args.stream().map(x -> x.clone()).collect(Collectors.toList()));
  }

  /**
   * Inserts an argument at the given index, moving existing arguments down one place.
   *
   * @param index The index at which insertion should take place
   * @param arg The argument to be inserted
   */
  public void insertArg(int index, Expr arg) {
    args.add(index, arg);
  }

  @Override
  public boolean hasChild(IAstNode candidateChild) {
    return args.contains(candidateChild);
  }

  @Override
  public Expr getChild(int index) {
    if (index < 0 || index >= args.size()) {
      throw new IndexOutOfBoundsException("TypeConstructorExpr has no child at index " + index);
    }
    return args.get(index);
  }

  @Override
  public void setChild(int index, Expr expr) {
    if (index < 0 || index >= args.size()) {
      throw new IndexOutOfBoundsException("TypeConstructorExpr has no child at index " + index);
    }
    args.set(index, expr);
  }

  @Override
  public int getNumChildren() {
    return args.size();
  }

}
