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

package com.intellij.ide.highlighter.custom.tokens;


/**
 * @author dsl
 */
public abstract class BaseTokenParser implements TokenParser {
  protected CharSequence myBuffer;
  protected int myStartOffset;
  protected int myEndOffset;
  protected final TokenInfo myTokenInfo = new TokenInfo();

  public void setBuffer(CharSequence buffer, int startOffset, int endOffset) {
    myBuffer = buffer;
    myStartOffset = startOffset;
    myEndOffset = endOffset;
  }

  public void getTokenInfo(TokenInfo info) {
    info.updateData(myTokenInfo);
  }
}
