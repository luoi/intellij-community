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
package com.intellij.debugger.impl;

import com.intellij.debugger.engine.DebuggerUtils;
import com.intellij.debugger.settings.DebuggerSettings;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.DebuggingRunnerData;
import com.intellij.openapi.util.DefaultJDOMExternalizer;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.JDOMExternalizable;
import com.intellij.openapi.util.WriteExternalException;
import org.jdom.Element;

public class GenericDebuggerRunnerSettings implements JDOMExternalizable, DebuggingRunnerData {
  public String DEBUG_PORT = "";
  public int TRANSPORT = DebuggerSettings.SOCKET_TRANSPORT;
  public boolean LOCAL = true;

  public GenericDebuggerRunnerSettings() {
    try {
      DEBUG_PORT = DebuggerUtils.getInstance().findAvailableDebugAddress(DebuggerSettings.SOCKET_TRANSPORT == TRANSPORT);
    }
    catch (ExecutionException e) {
      DEBUG_PORT = "";
    }
  }

  public String getDebugPort() {
    return DEBUG_PORT;
  }

  public boolean isRemote() {
    return !LOCAL;
  }

  public void setLocal(boolean isLocal) {
    LOCAL = isLocal;
  }

  private void updateDefaultAddress() {
    boolean useDefaultPort = "".equals(DEBUG_PORT);

    if (getTransport() == DebuggerSettings.SOCKET_TRANSPORT) {
      try {
        Integer.parseInt(DEBUG_PORT);
      }
      catch (NumberFormatException e) {
        useDefaultPort = true;
      }
    }

    if (useDefaultPort) {
      try {
        DEBUG_PORT = DebuggerUtils.getInstance().findAvailableDebugAddress(getTransport() == DebuggerSettings.SOCKET_TRANSPORT);
      }
      catch (ExecutionException e) {
        DEBUG_PORT = "";
      }
    }
  }

  public void setDebugPort(String port) {
    DEBUG_PORT = port;
    updateDefaultAddress();
  }

  public void setTransport(int transport) {
    if (getTransport() != transport) {
      setDebugPort("");
    }
    TRANSPORT = transport;
    updateDefaultAddress();
  }

  public void readExternal(Element element) throws InvalidDataException {
    DefaultJDOMExternalizer.readExternal(this, element);
    updateDefaultAddress();
  }

  public void writeExternal(Element element) throws WriteExternalException {
    DefaultJDOMExternalizer.writeExternal(this, element);
  }

  public int getTransport() {
    if (LOCAL) {
      return DebuggerSettings.getInstance().DEBUGGER_TRANSPORT;
    }
    else {
      return TRANSPORT;
    }
  }
}
