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

import java.io.File;
import java.util.List;

import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.resources.InputFile;
import org.sonar.api.resources.Project;
import org.sonar.plugins.xaml.XamlLanguage;

/**
 * Common sensor class that works with files
 * @author Przemyslaw Kociolek
 */
public abstract class XamlFileSensor implements Sensor {

  private XamlLanguage language = new XamlLanguage();
  
  public boolean shouldExecuteOnProject(Project project) {
    return XamlLanguage.KEY.equals(project.getLanguageKey());
  }
  
  public void analyse(Project project, SensorContext context) {
    List<InputFile> sourceFiles = project.getFileSystem().mainFiles(XamlLanguage.KEY);

    for(InputFile inputFile : sourceFiles) {      
      if(shouldParseFile(inputFile.getFile())) {  
        parseFile(inputFile, project, context);
      }
    } 
  }
  
  protected boolean shouldParseFile(File file) {
    for(String suffix : language.getSourceFileSuffixes()) {
      if(file.getAbsolutePath().endsWith("."+suffix)) {
        return true;
      }
    }
    return false;
  }
  
  protected abstract void parseFile(InputFile file, Project project, SensorContext context);
  
}
