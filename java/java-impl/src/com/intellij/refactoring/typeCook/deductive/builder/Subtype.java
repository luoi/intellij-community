/*
 * Copyright 2000-2009 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.intellij.refactoring.typeCook.deductive.builder;

import com.intellij.psi.PsiType;
import com.intellij.refactoring.typeCook.deductive.resolver.Binding;

/**
 * Created by IntelliJ IDEA.
 * User: db
 * Date: Jul 20, 2004
 * Time: 6:02:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class Subtype extends Constraint {
  public Subtype(PsiType left, PsiType right) {
    super(left, right);
  }

  String relationString() {
    return "<:";
  }

  int relationType() {
    return 1;
  }

  public Constraint apply(final Binding b) {
    return new Subtype(b.apply(myLeft), b.apply(myRight));
  }
}
