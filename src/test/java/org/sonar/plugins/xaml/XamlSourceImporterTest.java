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

import org.sonar.plugins.xaml.XamlSourceImporter;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.List;

import org.apache.commons.configuration.Configuration;
import org.junit.Test;
import org.sonar.api.CoreProperties;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.resources.Project;
import org.sonar.api.resources.Resource;

public class XamlSourceImporterTest {
  
  public void testSourceImporter() {
    SensorContext context = mock(SensorContext.class);
    Project project = mockProject();
    XamlSourceImporter importer = new XamlSourceImporter(XamlTestUtils.mockXamlLanguage());

    importer.analyse(project, context);

    verify(context).saveSource((Resource) anyObject(), eq("<source>\n"));
  }

  private Project mockProject() {
    Project project = XamlTestUtils.mockProject();

    File sourceFile;
    File sourceDir;
    try{
      sourceFile = new File(getClass().getResource("/org/sonar/plugins/cxx/source.cc").toURI());
      sourceDir = new File(getClass().getResource("/org/sonar/plugins/cxx").toURI());
    } catch (java.net.URISyntaxException e) {
      System.out.println("Error while mocking project: " + e);
      return null;
    }

    List<File> sourceFiles = project.getFileSystem().getSourceFiles(XamlTestUtils.mockXamlLanguage());
    sourceFiles.clear();
    sourceFiles.add(sourceFile);
    List<File> sourceDirs = project.getFileSystem().getSourceDirs();
    sourceDirs.clear();
    sourceDirs.add(sourceDir);

    Configuration config = mock(Configuration.class);
    when(config.getBoolean(CoreProperties.CORE_IMPORT_SOURCES_PROPERTY,
                           CoreProperties.CORE_IMPORT_SOURCES_DEFAULT_VALUE)).thenReturn(true);
    when(project.getConfiguration()).thenReturn(config);

    return project;
  }
}
