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

package com.intellij.testIntegration;

import com.intellij.codeInsight.CodeInsightActionHandler;
import com.intellij.codeInsight.actions.BaseCodeInsightAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiUtilBase;
import com.intellij.idea.ActionsBundle;

public class GotoTestOrCodeAction extends BaseCodeInsightAction {
  protected CodeInsightActionHandler getHandler(){
    return new GotoTestOrCodeHandler();
  }

  protected boolean isValidForFile(Project project, Editor editor, final PsiFile file) {
    return true;
  }

  @Override
  public void update(AnActionEvent event) {
    Presentation p = event.getPresentation();
    p.setEnabled(false);
    Project project = event.getData(PlatformDataKeys.PROJECT);
    Editor editor = event.getData(PlatformDataKeys.EDITOR);
    if (editor == null || project == null) return;

    PsiFile psiFile = PsiUtilBase.getPsiFileInEditor(editor, project);
    if (psiFile == null) return;

    PsiElement element = GotoTestOrCodeHandler.getSelectedElement(editor, psiFile);

    if (element == null) return;

    p.setEnabled(true);
    if (TestFinderHelper.isTest(element)) {
      p.setText(ActionsBundle.message("action.GotoTestSubject.text"));
      p.setDescription(ActionsBundle.message("action.GotoTestSubject.description"));
    } else {
      p.setText(ActionsBundle.message("action.GotoTest.text"));
      p.setDescription(ActionsBundle.message("action.GotoTest.description"));
    }
  }
}
