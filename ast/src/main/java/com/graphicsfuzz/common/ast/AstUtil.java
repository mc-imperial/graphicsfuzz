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

import com.graphicsfuzz.common.ast.decl.Declaration;
import com.graphicsfuzz.common.ast.decl.FunctionDefinition;
import com.graphicsfuzz.common.ast.decl.FunctionPrototype;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class AstUtil {

  private AstUtil() {
    // Utility class should not be instantiable.
  }

  /**
   * Gets prototypes for all the functions declared, either via full declarations or
   * just prototypes, in the given shader.
   *
   * @param shader A shader from which function prototypes should be extracted
   * @return set of prototypes for all functions declared fully or via prototypes in the shader
   */
  public static List<FunctionPrototype> getFunctionPrototypesFromShader(TranslationUnit shader) {
    return getPrototypesForAllFunctions(shader.getTopLevelDeclarations());
  }

  /**
   * Gets prototypes for all the functions declarations or prototypes in the given list of
   * declarations.
   *
   * @param decls A list of declarations
   * @return set of prototypes for all functions declared fully or via prototypes in the declaration
   *         list
   */
  public static List<FunctionPrototype> getPrototypesForAllFunctions(List<Declaration> decls) {
    // Grab all prototypes associated with function definitions
    List<FunctionPrototype> result =
        getFunctionDefinitions(decls).map(x -> x.getPrototype()).collect(Collectors.toList());
    // Add any additional prototypes
    // TODO: we only check whether a function definition with a matching name is present;
    //       we should strengthen this to consider instead an exact prototype match.
    result.addAll(
        getFunctionPrototypes(decls).filter(
            x -> !getFunctionNames(result)
                .contains(x.getName())).collect(Collectors.toList()));
    return result;
  }

  private static Stream<FunctionDefinition> getFunctionDefinitions(List<Declaration> decls) {
    return decls.stream().filter(x -> x instanceof FunctionDefinition)
        .map(x -> ((FunctionDefinition) x));
  }

  private static Stream<FunctionPrototype> getFunctionPrototypes(List<Declaration> decls) {
    return decls.stream().filter(x -> x instanceof FunctionPrototype)
        .map(x -> (FunctionPrototype) x);
  }

  private static List<String> getFunctionNames(List<FunctionPrototype> prototypes) {
    return prototypes.stream().map(y -> y.getName()).collect(Collectors.toList());
  }

}
