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
package com.intellij.util.xml.highlighting;

import com.intellij.codeInsight.daemon.impl.analysis.XmlHighlightVisitor;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiElement;
import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.util.xml.GenericDomValue;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.Pair;
import org.jetbrains.annotations.NotNull;

/**
 * @author peter
*/
class DomElementResolveProblemDescriptorImpl extends DomElementProblemDescriptorImpl implements DomElementResolveProblemDescriptor {
  @NotNull private final PsiReference myReference;

  public DomElementResolveProblemDescriptorImpl(@NotNull final GenericDomValue domElement, @NotNull final PsiReference reference, LocalQuickFix... quickFixes) {
     super(domElement, XmlHighlightVisitor.getErrorDescription(reference), HighlightSeverity.ERROR, quickFixes);
     myReference = reference;
  }

  @NotNull
  public PsiReference getPsiReference() {
    return myReference;
  }

  @NotNull
  public GenericDomValue getDomElement() {
    return (GenericDomValue)super.getDomElement();
  }

  @NotNull
  protected Pair<TextRange, PsiElement> computeProblemRange() {
    final PsiReference reference = myReference;
    PsiElement element = reference.getElement();
    if (element instanceof XmlAttributeValue && element.getTextLength() == 0) return NO_PROBLEM;

    final TextRange referenceRange = reference.getRangeInElement();
    if (referenceRange.isEmpty()) {
      return element instanceof XmlAttributeValue
             ? Pair.create(TextRange.from(referenceRange.getStartOffset() - 1, 2), element)
             : Pair.create(TextRange.from(referenceRange.getStartOffset(), 1), element);
    }
    return Pair.create(referenceRange, element);
  }
}
