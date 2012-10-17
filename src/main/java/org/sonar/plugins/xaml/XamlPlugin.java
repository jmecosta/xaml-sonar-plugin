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
import org.sonar.api.Extension;
import org.sonar.api.Properties;
import org.sonar.api.Property;
import org.sonar.api.SonarPlugin;
import org.sonar.plugins.xaml.fxcop.XamlFxCopRulesRepository;
import org.sonar.plugins.xaml.fxcop.XamlFxCopRulesSensor;

@Properties({
    @Property(
      key = XamlPlugin.SOURCE_FILE_SUFFIXES_KEY,
      defaultValue = XamlLanguage.DEFAULT_SOURCE_SUFFIXES,
      name = "Source files suffixes",
      description = "Comma-separated list of suffixes for source files to analyze. Leave empty to use the default.",
      global = true,
      project = true),
    @Property(
      key = XamlFxCopRulesSensor.REPORT_PATH_KEY,
      defaultValue = "",
      name = "Path to customize report(s)",
      description = "Relative to projects' root. Ant patterns are accepted",
      global = false,
      project = true)     
      })

public final class XamlPlugin extends SonarPlugin {
  static final String SOURCE_FILE_SUFFIXES_KEY = "sonar.xaml.suffixes.sources";

  /**
   * {@inheritDoc}
   */
  public List<Class<? extends Extension>> getExtensions() {
    List<Class<? extends Extension>> l = new ArrayList<Class<? extends Extension>>();
    l.add(XamlLanguage.class);
    l.add(XamlSourceImporter.class);
    l.add(XamlDefaultProfile.class);
    l.add(XamlCodeColorizerFormat.class);    
    l.add(XamlLineCouterSensor.class);    
    l.add(XamlCpdMapping.class);    
    l.add(XamlFxCopRulesRepository.class);
    l.add(XamlFxCopRulesSensor.class);
    return l;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName();
  }
}
