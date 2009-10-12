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
package com.intellij.refactoring.ui;

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.refactoring.BaseRefactoringProcessor;
import com.intellij.refactoring.RefactoringBundle;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Author: msk
 */
public abstract class RefactoringDialog extends DialogWrapper {

  private Action myRefactorAction;
  private Action myPreviewAction;
  private boolean myCbPreviewResults;
  protected final Project myProject;

  protected RefactoringDialog(@NotNull Project project, boolean canBeParent) {
    super (project, canBeParent);
    myCbPreviewResults = true;
    myProject = project;
  }

  public final boolean isPreviewUsages() {
    return myCbPreviewResults;
  }

  protected void createDefaultActions() {
    super.createDefaultActions ();
    myRefactorAction = new RefactorAction();
    myPreviewAction = new PreviewAction();
  }

  /**
   * @return default implementation of "Refactor" action.
   */
  protected final Action getRefactorAction() {
    return myRefactorAction;
  }

  /**
   * @return default implementation of "Preview" action.
   */
  protected final Action getPreviewAction() {
    return myPreviewAction;
  }

  protected abstract void doAction();

  private void doPreviewAction () {
    myCbPreviewResults = true;
    doAction();
  }

  private void doRefactorAction () {
    myCbPreviewResults = false;
    doAction();
  }

  protected final void closeOKAction() { super.doOKAction(); }

  protected final void doOKAction() {
    doAction();
  }

  protected boolean areButtonsValid () { return true; }

  protected void canRun() throws ConfigurationException{
    if (!areButtonsValid()) throw new ConfigurationException(null);
  }

  protected void validateButtons() {
    boolean enabled = true;
    try {
      setErrorText(null);
      canRun();
    }
    catch (ConfigurationException e) {
      enabled = false;
      setErrorText(e.getMessage());
    }
    getPreviewAction().setEnabled(enabled);
    getRefactorAction().setEnabled(enabled);
  }

  protected boolean hasHelpAction () {
    return true;
  }

  protected Action[] createActions() {
    if (hasHelpAction ())
      return new Action[]{getRefactorAction(), getPreviewAction(), getCancelAction(), getHelpAction()};
    else
      return new Action[]{getRefactorAction(), getPreviewAction(), getCancelAction()};
  }

  protected Project getProject() {
    return myProject;
  }

  private class RefactorAction extends AbstractAction {
    public RefactorAction() {
      putValue(Action.NAME, RefactoringBundle.message("refactor.button"));
      putValue(DEFAULT_ACTION, Boolean.TRUE);
    }

    public void actionPerformed(ActionEvent e) {
      doRefactorAction ();
    }
  }

  private class PreviewAction extends AbstractAction {
    public PreviewAction() {
      putValue(Action.NAME, RefactoringBundle.message("preview.button"));
    }

    public void actionPerformed(ActionEvent e) {
      doPreviewAction ();
    }
  }

  protected void invokeRefactoring(BaseRefactoringProcessor processor) {
    final Runnable prepareSuccessfulCallback = new Runnable() {
      public void run() {
        close(DialogWrapper.OK_EXIT_CODE);
      }
    };
    processor.setPrepareSuccessfulSwingThreadCallback(prepareSuccessfulCallback);
    processor.setPreviewUsages(isPreviewUsages());
    processor.run();
  }
}
