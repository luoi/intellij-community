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
package org.jetbrains.plugins.groovy.lang.completion.smartEnter.fixers;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.plugins.groovy.lang.completion.smartEnter.GroovySmartEnterProcessor;
import org.jetbrains.plugins.groovy.lang.lexer.GroovyTokenTypes;
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.literals.GrString;

/**
 * User: Dmitry.Krasilschikov
 * Date: 04.08.2008
 */
public class GrLiteralFixer implements GrFixer {
  public void apply(Editor editor, GroovySmartEnterProcessor processor, PsiElement psiElement)
          throws IncorrectOperationException {

    if (psiElement.getNode().getElementType() == GroovyTokenTypes.mWRONG_STRING_LITERAL &&
            !StringUtil.endsWithChar(psiElement.getText(), '\'')) {
      editor.getDocument().insertString(psiElement.getTextRange().getEndOffset(), "\'");
    } else if (psiElement.getNode().getElementType() == GroovyTokenTypes.mWRONG_GSTRING_LITERAL &&
            !StringUtil.endsWithChar(psiElement.getText(), '\"')) {
      editor.getDocument().insertString(psiElement.getTextRange().getEndOffset(), "\"");
    } else if (psiElement instanceof GrString && !StringUtil.endsWithChar(psiElement.getText(), '\"')) {
      editor.getDocument().insertString(psiElement.getTextRange().getEndOffset(), "\"");
    }
  }
}
