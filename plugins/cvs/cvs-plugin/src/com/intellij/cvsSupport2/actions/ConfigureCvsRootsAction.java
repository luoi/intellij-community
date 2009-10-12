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
package com.intellij.cvsSupport2.actions;

import com.intellij.cvsSupport2.actions.cvsContext.CvsContextWrapper;
import com.intellij.openapi.vcs.actions.VcsContext;
import com.intellij.cvsSupport2.config.CvsApplicationLevelConfiguration;
import com.intellij.cvsSupport2.config.CvsRootConfiguration;
import com.intellij.cvsSupport2.config.ui.CvsConfigurationsListEditor;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.vcs.actions.VcsContext;

import java.util.ArrayList;
import java.util.List;

/**
 * author: lesya
 */
public class ConfigureCvsRootsAction extends CvsGlobalAction {

  public void actionPerformed(AnActionEvent e) {
    VcsContext cvsContext = CvsContextWrapper.createCachedInstance(e);
    CvsApplicationLevelConfiguration configuration = CvsApplicationLevelConfiguration.getInstance();
    List<CvsRootConfiguration> configurations = configuration.CONFIGURATIONS;
    CvsConfigurationsListEditor cvsConfigurationsListEditor =
      new CvsConfigurationsListEditor(new ArrayList<CvsRootConfiguration>(configurations), cvsContext.getProject());
    cvsConfigurationsListEditor.show();
    if (cvsConfigurationsListEditor.isOK()) {
      configuration.CONFIGURATIONS = cvsConfigurationsListEditor.getConfigurations();
    }

  }
}
