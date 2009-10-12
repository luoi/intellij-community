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

package com.intellij.compiler.impl.packagingCompiler;

import com.intellij.openapi.compiler.ValidityState;
import com.intellij.openapi.util.Comparing;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.SmartList;
import com.intellij.util.io.IOUtil;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.List;

/**
 * @author nik
 */
public class PackagingItemValidityState implements ValidityState {
  private final SmartList<Pair<String, Long>> myDestinations;

  public PackagingItemValidityState(List<DestinationInfo> destinationInfos) {
    myDestinations = new SmartList<Pair<String, Long>>();
    for (DestinationInfo info : destinationInfos) {
      final VirtualFile outputFile = info.getOutputFile();
      long timestamp = outputFile != null ? outputFile.getTimeStamp() : -1;
      myDestinations.add(Pair.create(info.getOutputPath(), timestamp));
    }
  }

  public PackagingItemValidityState(DataInput input) throws IOException {
    int size = input.readInt();
    myDestinations = new SmartList<Pair<String, Long>>();
    while (size-- > 0) {
      String path = IOUtil.readString(input);
      long timestamp = input.readLong();
      myDestinations.add(Pair.create(path, timestamp));
    }
  }

  public boolean equalsTo(final ValidityState otherState) {
    if (!(otherState instanceof PackagingItemValidityState)) {
      return false;
    }

    final SmartList<Pair<String, Long>> otherDestinations = ((PackagingItemValidityState)otherState).myDestinations;
    if (otherDestinations.size() != myDestinations.size()) {
      return false;
    }

    if (myDestinations.size() == 1) {
      return myDestinations.get(0).equals(otherDestinations.get(0));
    }

    return Comparing.haveEqualElements(myDestinations, otherDestinations);
  }


  public void save(final DataOutput output) throws IOException {
    output.writeInt(myDestinations.size());
    for (Pair<String, Long> pair : myDestinations) {
      IOUtil.writeString(pair.getFirst(), output);
      output.writeLong(pair.getSecond());
    }
  }
}
