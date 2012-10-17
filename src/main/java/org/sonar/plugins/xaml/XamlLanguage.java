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

import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;
import org.sonar.api.resources.AbstractLanguage;

/**
 * {@inheritDoc}
 */
public final class XamlLanguage extends AbstractLanguage {
  public static final String DEFAULT_SOURCE_SUFFIXES = "xaml";
  public static final String KEY = "xaml";
  
  private String[] sourceSuffixes;
  private String[] fileSuffixes;

  /**
   * {@inheritDoc}
   */
  public XamlLanguage(Configuration config) {
    super(KEY);
    sourceSuffixes = createStringArray(config.getStringArray(XamlPlugin.SOURCE_FILE_SUFFIXES_KEY), DEFAULT_SOURCE_SUFFIXES); 
    fileSuffixes = sourceSuffixes;
  }
  
  public XamlLanguage() {
    super(KEY);
    sourceSuffixes = createStringArray(null, DEFAULT_SOURCE_SUFFIXES); 
    fileSuffixes = sourceSuffixes;
  }
  
  /**
   * {@inheritDoc}
   */
  public String[] getFileSuffixes() {
    return fileSuffixes;
  }

  /**
   * @return  suffixes for xaml source files
   */
  public String[] getSourceFileSuffixes() {
    return sourceSuffixes;
  }

  private String[] createStringArray(String[] values, String defaultValues) {
    if(values == null || values.length == 0) {
      return StringUtils.split(defaultValues, ",");
    }
    return values;
  }
  
}
