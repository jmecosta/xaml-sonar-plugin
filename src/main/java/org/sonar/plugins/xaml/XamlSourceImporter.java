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

import org.sonar.api.batch.AbstractSourceImporter;

/**
 * {@inheritDoc}
 */
public final class XamlSourceImporter extends AbstractSourceImporter {
  
  /**
   * {@inheritDoc}
   */
  public XamlSourceImporter(XamlLanguage lang) {
    super(lang);
  }

  @Override
  public String toString() {
    return getClass().getSimpleName();
  }
}
