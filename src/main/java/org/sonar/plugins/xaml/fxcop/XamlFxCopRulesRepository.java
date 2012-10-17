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
package org.sonar.plugins.xaml.fxcop;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.sonar.api.platform.ServerFileSystem;
import org.sonar.api.rules.Rule;
import org.sonar.api.rules.RuleRepository;
import org.sonar.api.rules.XMLRuleParser;

/**
 * Loads the FXCop rules configuration file.
 */
public class XamlFxCopRulesRepository extends RuleRepository {

  // for user extensions
  private ServerFileSystem fileSystem;
  private XMLRuleParser xmlRuleParser;
  public static final String REPOSITORY_KEY = "fxcop";  

  public XamlFxCopRulesRepository(ServerFileSystem fileSystem, XMLRuleParser xmlRuleParser) {
    super(REPOSITORY_KEY, "xaml");
    setName(REPOSITORY_KEY);
    this.fileSystem = fileSystem;
    this.xmlRuleParser = xmlRuleParser;
  }
  
  @Override
  public List<Rule> createRules() {
    List<Rule> rules = new ArrayList<Rule>();
    rules.addAll(xmlRuleParser.parse(XamlFxCopRulesRepository.class.getResourceAsStream("/org/sonar/plugins/xaml/rules/rules.xml")));
    for (File userExtensionXml : fileSystem.getExtensions(REPOSITORY_KEY, "xml")) {
      rules.addAll(xmlRuleParser.parse(userExtensionXml));
    }
    return rules;
  }    
}
