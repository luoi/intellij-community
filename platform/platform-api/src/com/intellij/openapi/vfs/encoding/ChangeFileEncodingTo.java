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
package com.intellij.openapi.vfs.encoding;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.project.DumbAware;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.charset.Charset;

/**
 * @author cdr
*/
class ChangeFileEncodingTo extends AnAction implements DumbAware {
  private final VirtualFile myFile;
  private final Charset myCharset;

  ChangeFileEncodingTo(@Nullable VirtualFile file, @NotNull Charset charset) {
    super(charset.toString(),  "Change " + (file == null ? "default" : "file '"+file.getName()+"'") +
                               " encoding to '"+charset.displayName()+"'.", null);
    myFile = file;
    myCharset = charset;
  }

  public void actionPerformed(final AnActionEvent e) {
    chosen(myFile, myCharset);
  }

  protected void chosen(final VirtualFile file, final Charset charset) {
    EncodingManager.getInstance().setEncoding(file, charset);
  }
}
