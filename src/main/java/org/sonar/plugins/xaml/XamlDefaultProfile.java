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

import org.sonar.api.profiles.ProfileDefinition;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.profiles.XMLProfileParser;
import org.sonar.api.utils.ValidationMessages;

/**
 * {@inheritDoc}
 */
public class XamlDefaultProfile extends ProfileDefinition {
  private XMLProfileParser xmlProfileParser;

  /**
   * {@inheritDoc}
   */
  public XamlDefaultProfile(XMLProfileParser xmlProfileParser) {
    this.xmlProfileParser = xmlProfileParser;
  }

  @Override
  public RulesProfile createProfile(ValidationMessages messages) {
    RulesProfile profile = xmlProfileParser.parseResource(getClass().getClassLoader(),
                                                          "default-profile.xml", messages);
    profile.setDefaultProfile(true);
    profile.setProvided(true);
    return profile;
  }
}
