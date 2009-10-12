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

import com.intellij.codeInsight.daemon.impl.JavaReferenceImporter;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileTypes.StdFileTypes;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiCodeFragment;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.ui.EditorTextField;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;

/**
 * @author dsl
 */
public class CodeFragmentTableCellEditor extends AbstractCellEditor implements TableCellEditor {
  private Document myDocument;
  protected PsiCodeFragment myCodeFragment;
  private final Project myProject;
  private EditorTextField myEditorTextField;

  public CodeFragmentTableCellEditor(final Project project) {
    myProject = project;
  }

  public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
    myCodeFragment = (PsiCodeFragment)value;

    myDocument = PsiDocumentManager.getInstance(myProject).getDocument(myCodeFragment);
    myEditorTextField = new EditorTextField(myDocument, myProject, StdFileTypes.JAVA) {
      protected boolean shouldHaveBorder() {
        return false;
      }
    };
    return myEditorTextField;
  }

  public PsiCodeFragment getCellEditorValue() {
    return myCodeFragment;
  }

  public boolean stopCellEditing() {
    final Editor editor = myEditorTextField.getEditor();
    if (editor != null) {
      JavaReferenceImporter.autoImportReferenceAtCursor(editor, myCodeFragment, true);
    }
    super.stopCellEditing();
    PsiDocumentManager.getInstance(myProject).commitDocument(myDocument);
    return true;
  }
}
