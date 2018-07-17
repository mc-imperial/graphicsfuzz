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

package com.graphicsfuzz.astfuzzer.tool;

import com.graphicsfuzz.astfuzzer.util.BinaryInterchanger;
import com.graphicsfuzz.astfuzzer.util.ExprInterchanger;
import com.graphicsfuzz.astfuzzer.util.FunctionCallInterchanger;
import com.graphicsfuzz.astfuzzer.util.TernaryInterchanger;
import com.graphicsfuzz.astfuzzer.util.TypeConstructorInterchanger;
import com.graphicsfuzz.common.ast.decl.FunctionPrototype;
import com.graphicsfuzz.common.ast.expr.BinOp;
import com.graphicsfuzz.common.ast.type.BasicType;
import com.graphicsfuzz.common.ast.type.Type;
import com.graphicsfuzz.common.glslversion.ShadingLanguageVersion;
import com.graphicsfuzz.common.typing.TyperHelper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InterchangeablesGroupedBySignature {

  private final ShadingLanguageVersion shadingLanguageVersion;
  private final Map<Signature, List<ExprInterchanger>> interchangeableBySignature;


  public InterchangeablesGroupedBySignature(ShadingLanguageVersion shadingLanguageVersion) {
    this.shadingLanguageVersion = shadingLanguageVersion;
    interchangeableBySignature = processFunctionsMap();
    addTypeConstructors();
    addBinaries();
    addTernaries();
  }

  private static List<Type> genType() {
    return Arrays.asList(BasicType.FLOAT, BasicType.VEC2, BasicType.VEC3, BasicType.VEC4);
  }

  private static List<Type> igenType() {
    return Arrays.asList(BasicType.INT, BasicType.IVEC2, BasicType.IVEC3, BasicType.IVEC4);
  }

  private static List<Type> ugenType() {
    return Arrays.asList(BasicType.UINT, BasicType.UVEC2, BasicType.UVEC3, BasicType.UVEC4);
  }

  private void addTernaries() {
    {
      for (Type t : genType()) {
        addInterchangeable(new Signature(t, t, t), new TernaryInterchanger());
        addInterchangeable(new Signature(t, t), new TernaryInterchanger());
      }
    }
    {
      for (Type t : igenType()) {
        addInterchangeable(new Signature(t, t, t), new TernaryInterchanger());
        addInterchangeable(new Signature(t, t), new TernaryInterchanger());
      }
    }
    {
      for (Type t : ugenType()) {
        addInterchangeable(new Signature(t, t, t), new TernaryInterchanger());
        addInterchangeable(new Signature(t, t), new TernaryInterchanger());
      }
    }


  }

  private void addBinaries() {
    {
      final BinOp op = BinOp.MUL;
      for (Type t : genType()) {
        addInterchangeable(new Signature(TyperHelper.resolveTypeOfMul(t, t), t, t),
            new BinaryInterchanger(op));
      }
    }
    {
      final BinOp op = BinOp.ADD;
      for (Type t : genType()) {
        addInterchangeable(new Signature(TyperHelper.resolveTypeOfCommonBinary(t, t), t, t),
            new BinaryInterchanger(
                op));
      }
    }
    {
      final BinOp op = BinOp.SUB;
      for (Type t : genType()) {
        addInterchangeable(new Signature(TyperHelper.resolveTypeOfCommonBinary(t, t), t, t),
            new BinaryInterchanger(
                op));
      }
    }
    {
      final BinOp op = BinOp.DIV;
      for (Type t : genType()) {
        addInterchangeable(new Signature(TyperHelper.resolveTypeOfCommonBinary(t, t), t, t),
            new BinaryInterchanger(
                op));
      }
    }

  }

  private void addTypeConstructors() {
    {
      final String name = "float";
      for (Type t : genType()) {
        addInterchangeable(new Signature(BasicType.FLOAT, t),
            new TypeConstructorInterchanger(
                name));
      }
    }
    {
      final String name = "vec2";
      addInterchangeable(new Signature(BasicType.VEC2, BasicType.FLOAT),
          new TypeConstructorInterchanger(
              name));
      addInterchangeable(new Signature(BasicType.VEC2, BasicType.VEC2),
          new TypeConstructorInterchanger(
              name));
      addInterchangeable(new Signature(BasicType.VEC2, BasicType.FLOAT, BasicType.FLOAT),
          new TypeConstructorInterchanger(
              name));
    }
    {
      final String name = "vec3";
      for (Type t : genType()) {
        addInterchangeable(new Signature(BasicType.VEC3, BasicType.FLOAT),
            new TypeConstructorInterchanger(
                name));

        addInterchangeable(new Signature(BasicType.VEC3, BasicType.FLOAT,
            BasicType.FLOAT,
            BasicType.FLOAT), new TypeConstructorInterchanger(
            name));

        addInterchangeable(new Signature(BasicType.VEC3, BasicType.FLOAT,
            BasicType.VEC2), new TypeConstructorInterchanger(
            name));

        addInterchangeable(new Signature(BasicType.VEC3, BasicType.VEC2,
            BasicType.FLOAT), new TypeConstructorInterchanger(
            name));

        addInterchangeable(new Signature(BasicType.VEC3, BasicType.VEC3),
            new TypeConstructorInterchanger(
                name));
      }
    }
    {
      final String name = "vec4";
      addInterchangeable(new Signature(BasicType.VEC4, BasicType.FLOAT),
          new TypeConstructorInterchanger(
              name));
      addInterchangeable(new Signature(BasicType.VEC4, BasicType.FLOAT,
          BasicType.FLOAT,
          BasicType.FLOAT,
          BasicType.FLOAT), new TypeConstructorInterchanger(
          name));
      addInterchangeable(new Signature(BasicType.VEC4, BasicType.VEC3,
          BasicType.FLOAT), new TypeConstructorInterchanger(
          name));
      addInterchangeable(new Signature(BasicType.VEC4, BasicType.VEC3,
          BasicType.FLOAT), new TypeConstructorInterchanger(
          name));
      addInterchangeable(new Signature(BasicType.VEC4, BasicType.VEC2,
          BasicType.VEC2), new TypeConstructorInterchanger(
          name));
      addInterchangeable(new Signature(BasicType.VEC4, BasicType.VEC4),
          new TypeConstructorInterchanger(
              name));
    }
  }

  private void addInterchangeable(Signature key, ExprInterchanger value) {
    if (!interchangeableBySignature.containsKey(key)) {
      interchangeableBySignature.put(key, new ArrayList<>());
    }
    interchangeableBySignature.get(key).add(value);
  }

  private Map<Signature, List<ExprInterchanger>> processFunctionsMap() {

    Map<Signature, List<ExprInterchanger>> result = new HashMap<>();

    for (String functionName : TyperHelper.getBuiltins(shadingLanguageVersion).keySet()) {
      for (FunctionPrototype functionPrototype : TyperHelper.getBuiltins(shadingLanguageVersion)
          .get(functionName)) {

        Signature signature = convertFunction(functionPrototype);
        if (!result.containsKey(signature)) {
          result.put(signature, new ArrayList<>());
        }
        result.get(signature).add(new FunctionCallInterchanger(functionName));
      }
    }

    return result;
  }

  List<Signature> convertAll(List<FunctionPrototype> functionPrototypes) {
    List<Signature> result = new ArrayList<>();
    for (int i = 0; i < functionPrototypes.size(); i++) {
      result.add(convertFunction(functionPrototypes.get(i)));
    }

    return result;
  }

  Signature convertFunction(FunctionPrototype functionPrototype) {

    return new Signature(functionPrototype.getReturnType(),
        functionPrototype.getParameters().stream().map(item -> item.getType())
            .collect(Collectors.toList()));
  }

  List<ExprInterchanger> getInterchangeableForSignature(Signature signature) {

    return getInterchangeableForSignatureGivenMapping(signature, interchangeableBySignature);
  }

  List<ExprInterchanger> getInterchangeableForSignatureGivenMapping(
      Signature signature,
      Map<Signature, List<ExprInterchanger>> mapping) {

    List<ExprInterchanger> result = new ArrayList<>();

    mapping.keySet()
        .stream()
        .filter(item -> signature.matches(item))
        .forEach(item -> result.addAll(mapping.get(item)));
    return result;
  }

}