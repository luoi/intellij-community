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
package com.intellij.openapi.fileChooser.actions;

import com.intellij.ide.IdeBundle;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileChooser.ex.FileChooserDialogImpl;
import com.intellij.openapi.project.DumbAware;
import org.jetbrains.annotations.Nullable;

public class TooglePathShowingAction extends AnAction implements DumbAware {

  {
    setEnabledInModalContext(true);
  }

  public void update(final AnActionEvent e) {
    e.getPresentation().setText(IdeBundle.message("file.chooser.hide.path.tooltip.text"));
    final FileChooserDialogImpl dialog = getDialog(e);
    e.getPresentation().setEnabled(dialog != null);
  }

  private @Nullable
  FileChooserDialogImpl getDialog(final AnActionEvent e) {
    return e.getData(FileChooserDialogImpl.KEY);
  }

  public void actionPerformed(final AnActionEvent e) {
    final FileChooserDialogImpl dialog = getDialog(e);
    if (dialog != null) {
      dialog.toggleShowTextField();
    }
  }


}
