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

package com.graphicsfuzz.common.util;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.TerminalNode;

public class TimeoutParseTreeListener implements ParseTreeListener {

  private final long timeToStop;

  public TimeoutParseTreeListener(long timeToStop) {
    this.timeToStop = timeToStop;
  }

  private void checkTime() {
    if (System.currentTimeMillis() > timeToStop) {
      throw new ParseTimeoutRuntimeException();
    }
  }

  @Override
  public void visitTerminal(TerminalNode terminalNode) {
    checkTime();
  }

  @Override
  public void visitErrorNode(ErrorNode errorNode) {
    checkTime();
  }

  @Override
  public void enterEveryRule(ParserRuleContext parserRuleContext) {
    checkTime();
  }

  @Override
  public void exitEveryRule(ParserRuleContext parserRuleContext) {
    checkTime();
  }
}
