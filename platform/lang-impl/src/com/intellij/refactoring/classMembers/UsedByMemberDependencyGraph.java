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

package com.intellij.refactoring.classMembers;

import com.intellij.lang.LanguageDependentMembersRefactoringSupport;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.NavigatablePsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.PsiElement;
import com.intellij.refactoring.RefactoringBundle;
import com.intellij.util.containers.HashMap;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class UsedByMemberDependencyGraph<T extends NavigatablePsiElement, C extends PsiElement, M extends MemberInfoBase<T>> implements MemberDependencyGraph<T, M> {
  protected HashSet<T> mySelectedNormal;
  protected HashSet<T> mySelectedAbstract;
  protected HashSet<T> myMembers;
  protected HashSet<T> myDependencies = null;
  protected HashMap<T, HashSet<T>> myDependenciesToDependent = null;
  private final MemberDependenciesStorage<T, C> myMemberDependenciesStorage;

  UsedByMemberDependencyGraph(C aClass) {
    myMemberDependenciesStorage = new MemberDependenciesStorage<T, C>(aClass, null);
    mySelectedNormal = new HashSet<T>();
    mySelectedAbstract = new HashSet<T>();
    myMembers = new HashSet<T>();
  }

  public void memberChanged(M memberInfo) {
    final ClassMembersRefactoringSupport support =
      LanguageDependentMembersRefactoringSupport.INSTANCE.forLanguage(memberInfo.getMember().getLanguage());
    if (support != null && support.isProperMember(memberInfo)) {
      myDependencies = null;
      myDependenciesToDependent = null;
      T member = memberInfo.getMember();
      myMembers.add(member);
      if (!memberInfo.isChecked()) {
        mySelectedNormal.remove(member);
        mySelectedAbstract.remove(member);
      }
      else {
        if (memberInfo.isToAbstract()) {
          mySelectedNormal.remove(member);
          mySelectedAbstract.add(member);
        }
        else {
          mySelectedNormal.add(member);
          mySelectedAbstract.remove(member);
        }
      }
    }
  }

  public Set<? extends T> getDependent() {
    if(myDependencies == null) {
      myDependencies = new HashSet<T>();
      myDependenciesToDependent = new HashMap<T, HashSet<T>>();
      for (T member : myMembers) {
        Set<T> dependent = myMemberDependenciesStorage.getMemberDependencies(member);
        for (final T aDependent : dependent) {
          if (mySelectedNormal.contains(aDependent) && !mySelectedAbstract.contains(aDependent)) {
            myDependencies.add(member);
            HashSet<T> deps = myDependenciesToDependent.get(member);
            if (deps == null) {
              deps = new HashSet<T>();
              myDependenciesToDependent.put(member, deps);
            }
            deps.add(aDependent);
          }
        }
      }
    }

    return myDependencies;
  }

  public Set<? extends T> getDependenciesOf(T member) {
    final Set<? extends T> dependent = getDependent();
    if(!dependent.contains(member)) return null;
    return myDependenciesToDependent.get(member);
  }

  public String getElementTooltip(T element) {
    final Set<? extends T> dependencies = getDependenciesOf(element);
    if (dependencies == null || dependencies.size() == 0) return null;

    ArrayList<String> strings = new ArrayList<String>();
    for (T dep : dependencies) {
      if (dep instanceof PsiNamedElement) {
        strings.add(((PsiNamedElement)dep).getName());
      }
    }

    if (strings.isEmpty()) return null;
    return RefactoringBundle.message("uses.0", StringUtil.join(strings, ", "));
  }
}
