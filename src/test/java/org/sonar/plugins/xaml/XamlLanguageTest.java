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

import org.apache.commons.configuration.BaseConfiguration;
import org.apache.commons.configuration.Configuration;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;

public class XamlLanguageTest {
  
  private Configuration config;
  
  @Before
  public void setup() {
    config = new BaseConfiguration();
  }
  
  @Test
  public void shouldReturnConfiguredFileSuffixes() {
    config.setProperty(XamlPlugin.SOURCE_FILE_SUFFIXES_KEY, "xaml,Xaml,XAML");
    XamlLanguage xaml = new XamlLanguage(config);

    String[] expected = {"xaml", "Xaml", "XAML"};
    String[] expectedSources = {"xaml", "Xaml", "XAML"};
    
    assertThat(xaml.getFileSuffixes(), is(expected));
    assertThat(xaml.getSourceFileSuffixes(), is(expectedSources));
  }
  
  @Test
  public void shouldReturnDefaultFileSuffixes() {
    XamlLanguage xaml = new XamlLanguage(config);
    
    String[] expectedSources = {"xaml"};
    String[] expectedAll = {"xaml"};
    
    assertThat(xaml.getFileSuffixes(), is(expectedAll));
    assertThat(xaml.getSourceFileSuffixes(), is(expectedSources));
  }       
}
