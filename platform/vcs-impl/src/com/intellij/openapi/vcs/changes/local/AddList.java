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
package com.intellij.openapi.vcs.changes.local;

import com.intellij.openapi.vcs.changes.ChangeListListener;
import com.intellij.openapi.vcs.changes.ChangeListWorker;
import com.intellij.openapi.vcs.changes.LocalChangeList;
import com.intellij.util.EventDispatcher;

public class AddList implements ChangeListCommand {
  private final String myName;
  private final String myComment;
  private LocalChangeList myNewListCopy;

  public AddList(final String name, final String comment) {
    myName = name;
    myComment = comment;
  }

  public void apply(final ChangeListWorker worker) {
    myNewListCopy = worker.addChangeList(myName, myComment);
  }

  public void doNotify(final EventDispatcher<ChangeListListener> dispatcher) {
    dispatcher.getMulticaster().changeListAdded(myNewListCopy);
  }

  public LocalChangeList getNewListCopy() {
    return myNewListCopy;
  }
}
