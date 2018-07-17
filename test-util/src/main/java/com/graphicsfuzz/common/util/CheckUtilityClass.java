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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class CheckUtilityClass {

  /**
   * Checks that a utility class is well-formed; e.g., that it is
   * final, with a single private constructor and no non-static
   * methods
   * @param utilityClass The class to check
   */
  public static void assertUtilityClassWellDefined(final Class<?> utilityClass)
      throws NoSuchMethodException, InvocationTargetException,
      InstantiationException, IllegalAccessException {
    assertTrue("Utility class must be final",
        Modifier.isFinal(utilityClass.getModifiers()));
    assertEquals("Utility class can only have one constructor", 1,
        utilityClass.getDeclaredConstructors().length);
    final Constructor<?> constructor = utilityClass.getDeclaredConstructor();
    if (constructor.isAccessible()
          || !Modifier.isPrivate(constructor.getModifiers())) {
      fail("Utility class constructor must be private");
    }
    constructor.setAccessible(true);
    constructor.newInstance();
    constructor.setAccessible(false);
    for (final Method method : utilityClass.getMethods()) {
      if (!Modifier.isStatic(method.getModifiers())
          && method.getDeclaringClass().equals(utilityClass)) {
        fail("Utility class can only have static methods; found non-static method:" + method);
      }
    }
  }

}
