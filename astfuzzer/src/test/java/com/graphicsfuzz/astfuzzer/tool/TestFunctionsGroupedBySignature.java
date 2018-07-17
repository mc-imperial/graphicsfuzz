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

import static junit.framework.TestCase.assertTrue;

import com.graphicsfuzz.astfuzzer.util.ExprInterchanger;
import com.graphicsfuzz.astfuzzer.util.FunctionCallInterchanger;
import com.graphicsfuzz.common.ast.type.BasicType;
import com.graphicsfuzz.common.ast.type.SamplerType;
import com.graphicsfuzz.common.glslversion.ShadingLanguageVersion;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.Before;
import org.junit.Test;

public class TestFunctionsGroupedBySignature {

  InterchangeablesGroupedBySignature interchangeablesGroupedBySignature;
  Map<Signature, List<ExprInterchanger>> functionsBySignature;

  static List<ExprInterchanger> getSpecificInterchangeableForSignatureGivenMapping(
      InterchangeablesGroupedBySignature interchangeablesGroupedBySignature,
      Signature signature,
      Class interchangerClass,
      Map<Signature, List<ExprInterchanger>> mapping) {

    return interchangeablesGroupedBySignature
        .getInterchangeableForSignatureGivenMapping(signature, mapping)
        .stream()
        .filter(item -> item.getClass().equals(interchangerClass))
        .collect(Collectors.toList());

  }

  @Before
  public void setUp() {

    interchangeablesGroupedBySignature = new InterchangeablesGroupedBySignature(
        ShadingLanguageVersion.ESSL_100);
    functionsBySignature = new HashMap<>();
    addDummyMappings();
  }

  private void addDummyMappings() {

    functionsBySignature
        .put(new Signature(null,
                new ArrayList<>(Arrays.asList(BasicType.INT))
            ),
            new ArrayList<>(Arrays.asList(new FunctionCallInterchanger("match"))));
    functionsBySignature
        .put(new Signature(BasicType.FLOAT,
                new ArrayList<>(Arrays.asList(BasicType.INT))
            ),
            new ArrayList<>(Arrays.asList(new FunctionCallInterchanger("float"),
                new FunctionCallInterchanger("floatCast"),
                new FunctionCallInterchanger("CastFloat"))));
    functionsBySignature
        .put(new Signature(BasicType.BOOL,
                new ArrayList<>(Arrays.asList(BasicType.INT))
            ),
            new ArrayList<>(Arrays.asList(new FunctionCallInterchanger("isNull"),
                new FunctionCallInterchanger("isNotNull"),
                new FunctionCallInterchanger("isOrIsNotNull"))));
    functionsBySignature
        .put(new Signature(null,
                new ArrayList<>(Arrays.asList(BasicType.INT, null))
            ),
            new ArrayList<>(Arrays.asList(new FunctionCallInterchanger("nullIntNull"),
                new FunctionCallInterchanger("NullintNull"),
                new FunctionCallInterchanger("nullintNull"))));
    functionsBySignature
        .put(new Signature(SamplerType.ISAMPLER1D,
                new ArrayList<>(Arrays.asList(BasicType.INT, SamplerType.SAMPLER2DARRAY))
            ),
            new ArrayList<>(Arrays.asList(new FunctionCallInterchanger("sampIntSamp"),
                new FunctionCallInterchanger("SampintSamp"),
                new FunctionCallInterchanger("sampintSamp"))));
    functionsBySignature
        .put(new Signature(null,
                new ArrayList<>(Arrays.asList(BasicType.FLOAT, null))
            ),
            new ArrayList<>(Arrays.asList(new FunctionCallInterchanger("nullFloatNull"),
                new FunctionCallInterchanger("NullFloatNull"),
                new FunctionCallInterchanger("nullfloatnull"))));
  }

  private List<String> getFunctionNames(List<ExprInterchanger> matches) {
    return matches.stream()
        .filter(item -> item instanceof FunctionCallInterchanger)
        .map(item -> (FunctionCallInterchanger) item)
        .map(item -> item.getName())
        .collect(Collectors.toList());
  }

  @Test
  public void testBasicFunctionality() {
    List<ExprInterchanger> matches = getSpecificInterchangeableForSignatureGivenMapping(
        interchangeablesGroupedBySignature,
        new Signature(BasicType.FLOAT,
            new ArrayList<>(Arrays.asList(BasicType.INT))
        )
        , FunctionCallInterchanger.class, functionsBySignature
    );

    List<String> names = getFunctionNames(matches);
    assertTrue(names.contains("match"));
    assertTrue(names.contains("float"));
    assertTrue(names.contains("floatCast"));
    assertTrue(names.contains("CastFloat"));

    assertTrue(matches.size() == 4);

  }

  @Test
  public void testNullMatchesEverything1Argument() {
    List<ExprInterchanger> matches = getSpecificInterchangeableForSignatureGivenMapping(
        interchangeablesGroupedBySignature,
        new Signature(null,
            new ArrayList<>(Arrays.asList(BasicType.INT))
        )
        , FunctionCallInterchanger.class, functionsBySignature
    );

    List<String> names = getFunctionNames(matches);

    assertTrue(names.contains("float"));
    assertTrue(names.contains("floatCast"));
    assertTrue(names.contains("CastFloat"));
    assertTrue(names.contains("isNull"));
    assertTrue(names.contains("isNotNull"));
    assertTrue(names.contains("isOrIsNotNull"));
    assertTrue(matches.size() == 7);
  }

  @Test
  public void testNullMatchesEverything2Arguments() {
    List<ExprInterchanger> matches = getSpecificInterchangeableForSignatureGivenMapping(
        interchangeablesGroupedBySignature,
        new Signature(null,
            new ArrayList<>(Arrays.asList(BasicType.INT, null))
        )
        , FunctionCallInterchanger.class
        , functionsBySignature
    );

    List<String> names = getFunctionNames(matches);

    assertTrue(names.contains("nullIntNull"));
    assertTrue(names.contains("NullintNull"));
    assertTrue(names.contains("nullintNull"));
    assertTrue(names.contains("sampIntSamp"));
    assertTrue(names.contains("SampintSamp"));
    assertTrue(names.contains("sampintSamp"));
    assertTrue(matches.size() == 6);
  }

  @Test
  public void testNullAnythingMatchesNull2Arguments() {
    List<ExprInterchanger> matches = getSpecificInterchangeableForSignatureGivenMapping(
        interchangeablesGroupedBySignature,
        new Signature(SamplerType.ISAMPLER1D,
            new ArrayList<>(Arrays.asList(BasicType.INT, SamplerType.SAMPLER2DARRAY))
        )
        , FunctionCallInterchanger.class
        , functionsBySignature
    );

    List<String> names = getFunctionNames(matches);

    assertTrue(names.contains("nullIntNull"));
    assertTrue(names.contains("NullintNull"));
    assertTrue(names.contains("nullintNull"));
    assertTrue(names.contains("sampIntSamp"));
    assertTrue(names.contains("SampintSamp"));
    assertTrue(names.contains("sampintSamp"));
    assertTrue(matches.size() == 6);
  }


}

