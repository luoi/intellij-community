/*
 * Copyright 2000-2005 JetBrains s.r.o.
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
package com.intellij.codeInsight;

import com.intellij.psi.PsiType;

/**
 * Created by IntelliJ IDEA.
 * User: ven
 * Date: Jul 14, 2004
 * Time: 4:45:46 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ExpectedTypeInfo {
  int TYPE_STRICTLY = 0;
  int TYPE_OR_SUBTYPE = 1;
  int TYPE_OR_SUPERTYPE = 2;
  ExpectedTypeInfo[] EMPTY = new ExpectedTypeInfo[0];

  PsiType getType ();

  PsiType getDefaultType ();

  int getKind();

  boolean equals (ExpectedTypeInfo info);

  String toString();

  ExpectedTypeInfo[] intersect(ExpectedTypeInfo info);

  boolean isArrayTypeInfo ();

  TailType getTailType();
}
