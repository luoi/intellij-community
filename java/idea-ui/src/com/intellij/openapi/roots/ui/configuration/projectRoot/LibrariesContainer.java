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
package com.intellij.openapi.roots.ui.configuration.projectRoot;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.OrderRootType;
import com.intellij.openapi.roots.libraries.Library;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author nik
 */
public interface LibrariesContainer {

  @Nullable
  Project getProject();

  enum LibraryLevel {GLOBAL, PROJECT, MODULE}

  @NotNull
  Library[] getLibraies(@NotNull LibraryLevel libraryLevel);

  @NotNull
  Library[] getAllLibraries();

  @NotNull
  VirtualFile[] getLibraryFiles(@NotNull Library library, @NotNull OrderRootType rootType);

  boolean canCreateLibrary(@NotNull LibraryLevel level);

  Library createLibrary(@NotNull @NonNls String name, @NotNull LibraryLevel level,
                        @NotNull VirtualFile[] classRoots, @NotNull VirtualFile[] sourceRoots);
}
