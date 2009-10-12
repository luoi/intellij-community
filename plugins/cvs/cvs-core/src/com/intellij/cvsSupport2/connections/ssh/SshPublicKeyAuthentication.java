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
package com.intellij.cvsSupport2.connections.ssh;

import com.intellij.openapi.util.io.FileUtil;
import com.trilead.ssh2.Connection;
import org.netbeans.lib.cvsclient.connection.AuthenticationException;

import java.io.File;
import java.io.IOException;

public class SshPublicKeyAuthentication implements SshAuthentication {
  private final String myLogin;
  private final File myFile;
  private final String myPassword;

  public SshPublicKeyAuthentication(final File file, final String password, final String login) {
    myFile = file;
    myPassword = password;
    myLogin = login;
  }

  public void authenticate(final Connection connection) throws AuthenticationException, SolveableAuthenticationException {
    char[] keyChars;
    try {
      keyChars = FileUtil.loadFileText(myFile);
    }
    catch (IOException e) {
      throw new SolveableAuthenticationException("Cannot load public key file.");
    }

    try {
      connection.authenticateWithPublicKey(myLogin, keyChars, myPassword);
    }
    catch (IOException e) {
      throw new SolveableAuthenticationException(e.getMessage(), e);
    }
  }
}
