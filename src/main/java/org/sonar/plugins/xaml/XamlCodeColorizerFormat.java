/*
 * Sonar Xaml Plugin, open source software quality management tool.
 * Author(s) : Jorge Costa
 * 
 * Sonar Xaml Plugin is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * Sonar Xaml Plugin is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 */

package org.sonar.plugins.xaml;

import java.util.ArrayList;
import java.util.List;
import org.sonar.api.web.CodeColorizerFormat;
import org.sonar.colorizer.RegexpTokenizer;
import org.sonar.colorizer.Tokenizer;

public class XamlCodeColorizerFormat extends CodeColorizerFormat {

  private final List<Tokenizer> tokenizers = new ArrayList<Tokenizer>();

  public XamlCodeColorizerFormat() {
    super(XamlLanguage.KEY);
    tokenizers.add(new RegexpTokenizer("<span class=\"k\">", "</span>", "</?\\p{L}*>?"));
    tokenizers.add(new RegexpTokenizer("<span class=\"k\">", "</span>", ">"));
  }

  @Override
  public List<Tokenizer> getTokenizers() {
    return tokenizers;
  }

}