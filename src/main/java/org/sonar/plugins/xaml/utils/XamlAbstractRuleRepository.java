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
package org.sonar.plugins.xaml.utils;

import java.io.InputStream;
import java.util.List;

import org.sonar.api.rules.Rule;
import org.sonar.api.rules.RuleRepository;
import org.sonar.api.rules.XMLRuleParser;
import org.sonar.plugins.xaml.XamlLanguage;

/**
 * {@inheritDoc}
 */
public abstract class XamlAbstractRuleRepository extends RuleRepository {

  protected abstract String fileName();

  /**
   * {@inheritDoc}
   */
  public XamlAbstractRuleRepository(String key) {
    super(key, XamlLanguage.KEY);
  }

  @Override
  public List<Rule> createRules() {
    final XMLRuleParser xmlParser = new XMLRuleParser();
    final InputStream xmlStream = getClass().getResourceAsStream(fileName());
    return xmlParser.parse(xmlStream);

  }

}
