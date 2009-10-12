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
package com.intellij.refactoring.introduceparameterobject.usageInfo;

import com.intellij.openapi.util.Comparing;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.PsiImmediateClassType;
import com.intellij.psi.util.TypeConversionUtil;
import com.intellij.refactoring.changeSignature.ChangeSignatureProcessor;
import com.intellij.refactoring.changeSignature.ParameterInfoImpl;
import com.intellij.refactoring.util.FixableUsageInfo;
import com.intellij.util.Function;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"MethodWithTooManyParameters"})
public class MergeMethodArguments extends FixableUsageInfo {
  private final PsiMethod method;
  private final PsiClass myContainingClass;
  private final boolean myKeepMethodAsDelegate;
  private final List<PsiTypeParameter> typeParams;
  private final String className;
  private final String packageName;
  private final String parameterName;
  private final int[] paramsToMerge;
  private final boolean lastParamIsVararg;

  public MergeMethodArguments(PsiMethod method,
                              String className,
                              String packageName,
                              String parameterName,
                              int[] paramsToMerge,
                              List<PsiTypeParameter> typeParams,
                              final boolean keepMethodAsDelegate, final PsiClass containingClass) {
    super(method);
    this.paramsToMerge = paramsToMerge;
    this.packageName = packageName;
    this.className = className;
    this.parameterName = parameterName;
    this.method = method;
    myContainingClass = containingClass;
    lastParamIsVararg = method.isVarArgs();
    myKeepMethodAsDelegate = keepMethodAsDelegate;
    this.typeParams = new ArrayList<PsiTypeParameter>(typeParams);
  }

  public void fixUsage() throws IncorrectOperationException {
    final JavaPsiFacade psiFacade = JavaPsiFacade.getInstance(method.getProject());
    final PsiMethod deepestSuperMethod = method.findDeepestSuperMethod();
    final PsiClass psiClass;
    if (myContainingClass != null) {
      psiClass = myContainingClass.findInnerClassByName(className, false);
    }
    else {
      psiClass = psiFacade.findClass(StringUtil.getQualifiedName(packageName, className));
    }
    PsiSubstitutor subst = PsiSubstitutor.EMPTY;
    if (deepestSuperMethod != null) {
      final PsiClass parentClass = deepestSuperMethod.getContainingClass();
      assert psiClass != null;
      final PsiSubstitutor parentSubstitutor =
        TypeConversionUtil.getSuperClassSubstitutor(parentClass, method.getContainingClass(), PsiSubstitutor.EMPTY);
      for (int i1 = 0; i1 < psiClass.getTypeParameters().length; i1++) {
        final PsiTypeParameter typeParameter = psiClass.getTypeParameters()[i1];
        for (PsiTypeParameter parameter : parentClass.getTypeParameters()) {
          if (Comparing.strEqual(typeParameter.getName(), parameter.getName())) {
            subst = subst.put(typeParameter, parentSubstitutor.substitute(
              new PsiImmediateClassType(parameter, PsiSubstitutor.EMPTY)));
            break;
          }
        }
      }
    }
    final List<ParameterInfoImpl> parametersInfo = new ArrayList<ParameterInfoImpl>();
    parametersInfo.add(new ParameterInfoImpl(-1, parameterName, new PsiImmediateClassType(psiClass, subst), null) {
      @Override
      public PsiExpression getValue(final PsiCallExpression expr) throws IncorrectOperationException {
        return psiFacade.getElementFactory().createExpressionFromText(getMergedParam(expr), expr);
      }
    });
    final PsiParameter[] parameters = method.getParameterList().getParameters();
    for (int i = 0; i < parameters.length; i++) {
      if (!isParameterToMerge(i)) {
        parametersInfo.add(new ParameterInfoImpl(i, parameters[i].getName(), parameters[i].getType()));
      }
    }

    new ChangeSignatureProcessor(method.getProject(), method, myKeepMethodAsDelegate, null, method.getName(), method.getReturnType(),
                                 parametersInfo.toArray(new ParameterInfoImpl[parametersInfo.size()])).run();
  }

  private boolean isParameterToMerge(int index) {
    for (int i : paramsToMerge) {
      if (i == index) {
        return true;
      }
    }
    return false;
  }

  private String getMergedParam(PsiCallExpression call) {
    final PsiExpression[] args = call.getArgumentList().getExpressions();
    StringBuffer newExpression = new StringBuffer();
    final String qualifiedName = myContainingClass != null ? myContainingClass.getQualifiedName() + "." + className : StringUtil.getQualifiedName(packageName, className);
    newExpression.append("new ").append(qualifiedName);
    if (!typeParams.isEmpty()) {
      final JavaResolveResult resolvant = call.resolveMethodGenerics();
      final PsiSubstitutor substitutor = resolvant.getSubstitutor();
      newExpression.append('<');
      final Map<PsiTypeParameter, PsiType> substitutionMap = substitutor.getSubstitutionMap();
      newExpression.append(StringUtil.join(typeParams, new Function<PsiTypeParameter, String>() {
        public String fun(final PsiTypeParameter typeParameter) {
          final PsiType boundType = substitutionMap.get(typeParameter);
          if (boundType != null) {
            return boundType.getCanonicalText();
          }
          else {
            return typeParameter.getName();
          }
        }
      }, ", "));
      newExpression.append('>');
    }
    newExpression.append('(');
    boolean isFirst = true;
    for (int index : paramsToMerge) {
      if (!isFirst) {
        newExpression.append(", ");
      }
      isFirst = false;
      newExpression.append(getArgument(args, index));
    }
    if (lastParamIsVararg) {
      final int lastArg = paramsToMerge[paramsToMerge.length - 1];
      for (int i = lastArg + 1; i < args.length; i++) {
        newExpression.append(',');
        newExpression.append(getArgument(args, i));
      }
    }
    newExpression.append(')');
    return newExpression.toString();
  }

  @Nullable
  private String getArgument(PsiExpression[] args, int i) {
    if (i < args.length) {
      return args[i].getText();
    }
    final PsiParameter[] parameters = method.getParameterList().getParameters();
    if (i < parameters.length) return parameters[i].getName();
    return null;
  }
}
