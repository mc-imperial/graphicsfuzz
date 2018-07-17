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

package com.graphicsfuzz.common.typing;

import com.graphicsfuzz.common.ast.decl.ParameterDecl;
import com.graphicsfuzz.common.ast.decl.VariableDeclInfo;
import com.graphicsfuzz.common.ast.decl.VariablesDeclaration;
import com.graphicsfuzz.common.ast.type.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

public class Scope {

  private final Map<String, ScopeEntry> variableMapping;
  private final Scope parent;

  public Scope(Scope parent) {
    this.variableMapping = new HashMap<>();
    this.parent = parent;
  }

  public void add(String name, Type type, Optional<ParameterDecl> parameterDecl,
        VariableDeclInfo declInfo,
        VariablesDeclaration variablesDecl) {
    checkNameTypeAndParam(name, type, parameterDecl);
    variableMapping.put(name, new ScopeEntry(type, parameterDecl, declInfo, variablesDecl));
  }

  public void add(String name, Type type, Optional<ParameterDecl> parameterDecl) {
    checkNameTypeAndParam(name, type, parameterDecl);
    variableMapping.put(name, new ScopeEntry(type, parameterDecl));
  }

  private void checkNameTypeAndParam(String name, Type type,
        Optional<ParameterDecl> parameterDecl) {
    if (type == null) {
      throw new RuntimeException("Attempt to register a variable '" + name + "' with null type");
    }
    if (variableMapping.containsKey(name)) {
      throw new DuplicateVariableException(name);
    }
    if (parameterDecl.isPresent() && parent != null && parent.parent != null) {
      throw new RuntimeException("Parameters cannot be added to a deeply nested scope");
    }
    assert name != null;
  }

  public Scope getParent() {
    return parent;
  }

  public ScopeEntry lookupScopeEntry(String name) {
    if (variableMapping.containsKey(name)) {
      return variableMapping.get(name);
    }
    if (parent != null) {
      return parent.lookupScopeEntry(name);
    }
    return null;
  }

  public Type lookupType(String name) {
    ScopeEntry entry = lookupScopeEntry(name);
    if (entry == null) {
      return null;
    }
    return entry.getType();
  }

  public List<String> namesOfAllVariablesInScope() {
    List<String> result = new ArrayList<>();
    Scope scope = this;
    while (scope != null) {
      result.addAll(scope.keys());
      scope = scope.parent;
    }
    result.sort(String::compareTo);
    return Collections.unmodifiableList(result);
  }

  public List<String> keys() {
    List<String> result = new ArrayList<>();
    result.addAll(variableMapping.keySet());
    result.sort(String::compareTo);
    return Collections.unmodifiableList(result);
  }

  public boolean hasParent() {
    return parent != null;
  }

  /**
   * Clones the scope, recursively cloning all parents, but does not deep-clone the mappings.
   * @return Cloned scope
   */
  public Scope shallowClone() {
    Scope result = new Scope(parent == null ? null : parent.shallowClone());
    for (Entry<String, ScopeEntry> entry : variableMapping.entrySet()) {
      result.variableMapping.put(entry.getKey(), entry.getValue());
    }
    return result;
  }

  /**
   * Removes and returns scope entry associated with the given name, if present.  Otherwise
   * delegates removal to parent scope, if one exists.  Otherwise returns null to indicate that
   * nothing was removed.
   * @param name Name of the key to be removed
   * @return Scope entry that has been removed, or null if no removal took place
   */
  public ScopeEntry remove(String name) {
    if (variableMapping.containsKey(name)) {
      return variableMapping.remove(name);
    }
    if (hasParent()) {
      return parent.remove(name);
    }
    return null;
  }

}
