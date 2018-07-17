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

import java.util.Random;

/**
 * Random generator that uses java.util.Random, to be used when genuine pseudo-random generation
 * is required (as opposed to mocking for testing).
 */
public class RandomWrapper implements IRandom {

  private final Random generator;

  public RandomWrapper(int seed) {
    this.generator = new Random(seed);
  }

  public RandomWrapper() {
    this.generator = new Random();
  }

  @Override
  public int nextInt(int bound) {
    return generator.nextInt(bound);
  }

  @Override
  public Float nextFloat() {
    return generator.nextFloat();
  }

  @Override
  public boolean nextBoolean() {
    return generator.nextBoolean();
  }

  @Override
  public IRandom spawnChild() {
    return new RandomWrapper(generator.nextInt());
  }

  public void setSeed(int seed) {
    generator.setSeed(seed);
  }

}
