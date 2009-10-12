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

package com.intellij.uiDesigner.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.util.IconLoader;
import com.intellij.uiDesigner.CaptionSelection;
import com.intellij.uiDesigner.FormEditingUtil;
import com.intellij.uiDesigner.designSurface.GuiEditor;
import org.jetbrains.annotations.NonNls;

/**
 * @author yole
 */
public abstract class RowColumnAction extends AnAction {
  private final String myColumnText;
  private final String myColumnIcon;
  private final String myRowText;
  private final String myRowIcon;

  public RowColumnAction(final String columnText, @NonNls final String columnIcon,
                         final String rowText, @NonNls final String rowIcon) {
    myColumnText = columnText;
    myColumnIcon = columnIcon;
    myRowText = rowText;
    myRowIcon = rowIcon;
  }

  public void actionPerformed(final AnActionEvent e) {
    GuiEditor editor = FormEditingUtil.getEditorFromContext(e.getDataContext());
    CaptionSelection selection = (CaptionSelection) e.getDataContext().getData(CaptionSelection.class.getName());
    if (editor == null || selection == null || !editor.ensureEditable()) {
      return;
    }
    actionPerformed(selection);
    selection.getContainer().revalidate();
    editor.refreshAndSave(true);
  }

  protected abstract void actionPerformed(CaptionSelection selection);

  public void update(final AnActionEvent e) {
    final Presentation presentation = e.getPresentation();
    CaptionSelection selection = (CaptionSelection) e.getDataContext().getData(CaptionSelection.class.getName());
    if (selection == null) {
      presentation.setEnabled(false);
    }
    else {
      presentation.setEnabled(selection.getContainer() != null && selection.getFocusedIndex() >= 0);
      if (!selection.isRow()) {
        presentation.setText(myColumnText);
        if (myColumnIcon != null) {
          presentation.setIcon(IconLoader.getIcon(myColumnIcon));
        }
      }
      else {
        presentation.setText(myRowText);
        if (myRowIcon != null) {
          presentation.setIcon(IconLoader.getIcon(myRowIcon));
        }
      }
    }
  }
}
