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

package com.intellij.ide.highlighter.custom.impl;

import com.intellij.openapi.fileTypes.SyntaxHighlighterProvider;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.impl.AbstractFileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ide.highlighter.custom.CustomFileHighlighter;
import org.jetbrains.annotations.Nullable;

/**
 * @author yole
 */
public class CustomFileTypeHighlighterProvider implements SyntaxHighlighterProvider {
  @Nullable
  public SyntaxHighlighter create(final FileType fileType, @Nullable final Project project, @Nullable final VirtualFile file) {
    if (fileType instanceof AbstractFileType) {
      return new CustomFileHighlighter(((AbstractFileType) fileType).getSyntaxTable());
    }
    return null;
  }
}