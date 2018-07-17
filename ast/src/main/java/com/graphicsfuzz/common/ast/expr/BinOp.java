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

public enum BinOp implements Op {
  COMMA,
  MOD,
  MUL,
  DIV,
  ADD,
  SUB,
  BAND,
  BOR,
  BXOR,
  LAND,
  LOR,
  LXOR,
  SHL,
  SHR,
  LT,
  GT,
  LE,
  GE,
  EQ,
  NE,
  ASSIGN,
  MUL_ASSIGN,
  DIV_ASSIGN,
  MOD_ASSIGN,
  ADD_ASSIGN,
  SUB_ASSIGN,
  BAND_ASSIGN,
  BOR_ASSIGN,
  BXOR_ASSIGN,
  SHL_ASSIGN,
  SHR_ASSIGN;

  @Override
  public String getText() {
    switch (this) {
      case COMMA:
        return ",";
      case MOD:
        return "%";
      case MUL:
        return "*";
      case DIV:
        return "/";
      case ADD:
        return "+";
      case SUB:
        return "-";
      case BAND:
        return "&";
      case BOR:
        return "|";
      case BXOR:
        return "^";
      case LAND:
        return "&&";
      case LOR:
        return "||";
      case LXOR:
        return "^^";
      case SHL:
        return "<<";
      case SHR:
        return ">>";
      case LT:
        return "<";
      case GT:
        return ">";
      case LE:
        return "<=";
      case GE:
        return ">=";
      case EQ:
        return "==";
      case NE:
        return "!=";
      case ASSIGN:
        return "=";
      case MUL_ASSIGN:
        return "*=";
      case DIV_ASSIGN:
        return "/=";
      case MOD_ASSIGN:
        return "%=";
      case ADD_ASSIGN:
        return "+=";
      case SUB_ASSIGN:
        return "-=";
      case BAND_ASSIGN:
        return "&=";
      case BOR_ASSIGN:
        return "|=";
      case BXOR_ASSIGN:
        return "^=";
      case SHL_ASSIGN:
        return "<<=";
      default:
        assert this == SHR_ASSIGN : "Unknown binary operator " + this;
        return ">>=";
    }
  }

  @Override
  public boolean isSideEffecting() {
    switch (this) {
      case ASSIGN:
      case MUL_ASSIGN:
      case DIV_ASSIGN:
      case MOD_ASSIGN:
      case ADD_ASSIGN:
      case SUB_ASSIGN:
      case BAND_ASSIGN:
      case BOR_ASSIGN:
      case BXOR_ASSIGN:
      case SHL_ASSIGN:
      case SHR_ASSIGN:
        return true;
      default:
        return false;
    }
  }

}
