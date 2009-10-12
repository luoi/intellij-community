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
package com.intellij.spellchecker.tokenizer;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNameIdentifierOwner;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by IntelliJ IDEA.
 *
 * @author shkate@jetbrains.com
 */
public class PsiIdentifierOwnerTokenizer extends Tokenizer<PsiNameIdentifierOwner> {

  @Nullable
  @Override
  public Token[] tokenize(@NotNull PsiNameIdentifierOwner element) {
    PsiElement identifier = element.getNameIdentifier();
    if (identifier == null) {
      return null;
    }
    int offset = identifier.getTextRange().getStartOffset() - element.getTextRange().getStartOffset();
    return new Token[]{new Token<PsiElement>(element, identifier.getText(), true, offset)};
  }


}