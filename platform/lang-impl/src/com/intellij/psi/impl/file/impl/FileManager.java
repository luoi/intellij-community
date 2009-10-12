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

package com.intellij.psi.impl.file.impl;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.GlobalSearchScope;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;

import java.util.List;

public interface FileManager {
  void dispose();

  void runStartupActivity();

  @Nullable
  PsiFile findFile(@NotNull VirtualFile vFile);

  @Nullable
  PsiDirectory findDirectory(@NotNull VirtualFile vFile);

  void reloadFromDisk(@NotNull PsiFile file); //Q: move to PsiFile(Impl)?

  @Nullable
  PsiFile getCachedPsiFile(@NotNull VirtualFile vFile);

  @NotNull GlobalSearchScope getResolveScope(@NotNull PsiElement element);
  @NotNull GlobalSearchScope getUseScope(@NotNull PsiElement element);

  @TestOnly
  void cleanupForNextTest();

  FileViewProvider findViewProvider(VirtualFile file);
  FileViewProvider findCachedViewProvider(VirtualFile file);
  void setViewProvider(VirtualFile virtualFile, FileViewProvider fileViewProvider);

  List<PsiFile> getAllCachedFiles();

  FileViewProvider createFileViewProvider(VirtualFile file, boolean physical);
}
