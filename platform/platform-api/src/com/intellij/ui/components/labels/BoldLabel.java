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
package com.intellij.ui.components.labels;

import javax.swing.*;

/**
 * @author kir
 */
@SuppressWarnings({"ClassWithTooManyConstructors"})
public class BoldLabel extends JLabel {

  public BoldLabel() {
  }

  public BoldLabel(String text) {
    super(toHtml(text));
  }

  public BoldLabel(String text, int horizontalAlignment) {
    super(toHtml(text), horizontalAlignment);
  }

  public BoldLabel(Icon image) {
    super(image);
  }

  public BoldLabel(Icon image, int horizontalAlignment) {
    super(image, horizontalAlignment);
  }

  public BoldLabel(String text, Icon icon, int horizontalAlignment) {
    super(toHtml(text), icon, horizontalAlignment);
  }

  public void setText(String text) {
    super.setText(toHtml(text));
  }

  private static String toHtml(String text) {
    if (text.startsWith("<html>")) return text;
    return "<html><b>" + text.replaceAll("\\n", "<br>") + "</b></html>";
  }

  @SuppressWarnings({"MagicNumber"})
  public static void main(String[] args) {
    JFrame frame = new JFrame();
    frame.getContentPane().add(new BoldLabel("bebebe\nlalala\nfufufu"));
    frame.setSize(400, 300);
    frame.setVisible(true);
  }
}
