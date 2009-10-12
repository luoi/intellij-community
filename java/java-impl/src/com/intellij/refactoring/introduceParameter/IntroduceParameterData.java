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
package com.intellij.refactoring.introduceParameter;

import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiExpression;
import com.intellij.psi.PsiLocalVariable;
import com.intellij.psi.PsiType;
import gnu.trove.TIntArrayList;

public interface IntroduceParameterData {
  @NotNull
  Project getProject();

  PsiMethod getMethodToReplaceIn();

  @NotNull
  PsiMethod getMethodToSearchFor();

  PsiExpression getParameterInitializer();

  PsiExpression getExpressionToSearch();

  PsiLocalVariable getLocalVariable();

  boolean isRemoveLocalVariable();

  @NotNull
  String getParameterName();

  boolean isReplaceAllOccurences();

  int getReplaceFieldsWithGetters();

  boolean isDeclareFinal();

  boolean isGenerateDelegate();

  PsiType getForcedType();

  @NotNull
  TIntArrayList getParametersToRemove();
}
