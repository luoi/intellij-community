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
package com.intellij.util.net;

import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.util.DefaultJDOMExternalizer;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.JDOMExternalizable;
import com.intellij.openapi.util.WriteExternalException;
import com.intellij.ui.GuiUtils;
import org.apache.commons.codec.binary.Base64;
import org.jdom.Element;
import org.jetbrains.annotations.Nls;

import javax.swing.*;
import java.io.IOException;
import java.net.*;

/**
 * Created by IntelliJ IDEA.
 * User: stathik
 * Date: Oct 7, 2003
 * Time: 3:58:23 PM
 * To change this template use Options | File Templates.
 */
public class HttpConfigurable implements JDOMExternalizable, ApplicationComponent, SearchableConfigurable {
  public boolean USE_HTTP_PROXY = false;
  public String PROXY_HOST = "";
  public int PROXY_PORT = 80;

  public boolean PROXY_AUTHENTICATION = false;
  public String PROXY_LOGIN = "";
  public String PROXY_PASSWORD_CRYPT = "";
  public boolean KEEP_PROXY_PASSWORD = false;

  private HTTPProxySettingsPanel myPanel;

  public static HttpConfigurable getInstance() {
    return ServiceManager.getService(HttpConfigurable.class);
  }

  public void readExternal(Element element) throws InvalidDataException {
    DefaultJDOMExternalizer.readExternal(this, element);
    if (!KEEP_PROXY_PASSWORD)
      PROXY_PASSWORD_CRYPT = "";
  }

  public void writeExternal(Element element) throws WriteExternalException {
    String proxyPassword = PROXY_PASSWORD_CRYPT;
    if (!KEEP_PROXY_PASSWORD)
      PROXY_PASSWORD_CRYPT = "";

    DefaultJDOMExternalizer.writeExternal(this, element);

    PROXY_PASSWORD_CRYPT = proxyPassword;
  }

  public String getComponentName() {
    return "HttpConfigurable";
  }

  public void initComponent() {
  }

  public void disposeComponent() {
  }

  public String getPlainProxyPassword () {
    return new String(new Base64().decode(PROXY_PASSWORD_CRYPT.getBytes()));
  }

  public void setPlainProxyPassword (String password) {
    PROXY_PASSWORD_CRYPT = new String(new Base64().encode(password.getBytes()));
  }

  public PasswordAuthentication getPromptedAuthentication(final String host, final String prompt) {
    if (PROXY_AUTHENTICATION &&
        ! KEEP_PROXY_PASSWORD) {
      Runnable runnable = new Runnable() {
        public void run() {
          AuthenticationDialog dlg = new AuthenticationDialog(host, prompt);
          dlg.show();
        }
      };
      try {
        GuiUtils.runOrInvokeAndWait(runnable);
      }
      catch (Exception e) {
        // ignore
      }
    }
    return new PasswordAuthentication(PROXY_LOGIN, getPlainProxyPassword().toCharArray());
  }

  private Authenticator getAuthenticator () {
    return new Authenticator () {
      protected PasswordAuthentication getPasswordAuthentication() {
        return getPromptedAuthentication(getRequestingHost(), getRequestingPrompt());
      }
    };
  }

  // @todo [all] Call this function before every HTTP connection.
  /**
   * Call this function before every HTTP connection.
   * If system configured to use HTTP proxy, this function
   * checks all required parameters and ask password if
   * required.
   * @param url URL for HTTP connection
   * @throws IOException
   */
  public void prepareURL (String url) throws IOException {
    setAuthenticator();

    URLConnection connection = new URL (url).openConnection();
    connection.setConnectTimeout(3 * 1000);
    connection.setReadTimeout(3 * 1000);
    connection.connect();
    try {
      connection.getInputStream();
    }
    catch (Throwable e) {
      if (e instanceof IOException) {
        throw (IOException)e;
      }
    }
    if (connection instanceof HttpURLConnection) {
      ((HttpURLConnection)connection).disconnect();
    }
  }

  public void setAuthenticator() {
    if (USE_HTTP_PROXY) {
      System.setProperty("proxySet", "true");
      System.setProperty("http.proxyHost", PROXY_HOST);
      System.setProperty("http.proxyPort", Integer.toString (PROXY_PORT));
      Authenticator.setDefault(getAuthenticator());
    } else {
      System.setProperty("proxySet", "false");
      System.clearProperty("http.proxyHost");
      System.clearProperty("http.proxyPort");
      Authenticator.setDefault(null);
    }
  }

  public String getId() {
    return getHelpTopic();
  }

  public Runnable enableSearch(final String option) {
    return null;
  }

  @Nls
  public String getDisplayName() {
    return "HTTP Proxy";
  }

  public Icon getIcon() {
    return null;
  }

  public String getHelpTopic() {
    return "http.proxy";
  }

  public JComponent createComponent() {
    if (myPanel == null) {
      myPanel = new HTTPProxySettingsPanel(getInstance());
    }
    return myPanel.getComponent();
  }

  public boolean isModified() {
    return myPanel != null && myPanel.isModified();
  }

  public void apply() throws ConfigurationException {
    if (myPanel != null) {
      myPanel.apply();
    }
  }

  public void reset() {
    if (myPanel != null) {
      myPanel.reset();
    }
  }

  public void disposeUIResources() {
    myPanel = null;
  }
}
