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

import net.sourceforge.pmd.cpd.AnyLanguage;
import net.sourceforge.pmd.cpd.Tokenizer;
import org.sonar.api.batch.AbstractCpdMapping;
import org.sonar.api.resources.Language;

/**
 * {@inheritDoc}
 */
public final class XamlCpdMapping extends AbstractCpdMapping {

  private final AnyLanguage language = new AnyLanguage("xaml");
  private XamlLanguage lang;

  /**
   *  {@inheritDoc}
   */
  public XamlCpdMapping(XamlLanguage lang) {
    this.lang = lang;
  }

  /**
   *  {@inheritDoc}
   */
  public Language getLanguage() {
    return lang;
  }

  /**
   *  {@inheritDoc}
   */
  public Tokenizer getTokenizer() {
    return language.getTokenizer();
  }
}
