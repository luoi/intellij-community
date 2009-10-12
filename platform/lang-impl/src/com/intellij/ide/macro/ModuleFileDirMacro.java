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

package com.intellij.ide.macro;

import com.intellij.ide.DataAccessors;
import com.intellij.ide.IdeBundle;
import com.intellij.openapi.actionSystem.DataContext;

import java.io.File;

public final class ModuleFileDirMacro extends Macro {
  public String getName() {
    return "ModuleFileDir";
  }

  public String getDescription() {
    return IdeBundle.message("macro.module.file.directory");
  }

  public String expand(DataContext dataContext) {
    final String path = DataAccessors.MODULE_FILE_PATH.from(dataContext);
    if (path == null) {
      return null;
    }
    final File fileDir = new File(path).getParentFile();
    if (fileDir == null) {
      return null;
    }
    return fileDir.getPath();
  }
}