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

import org.sonar.plugins.xaml.utils.XamlFileSensor;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.resources.InputFile;
import org.sonar.api.resources.Project;
import org.sonar.plugins.xaml.XamlTestUtils;


class XamlFileSensorImpl extends XamlFileSensor
{

  List<InputFile> files = new ArrayList<InputFile>();
  
  @Override
  protected void parseFile(InputFile file, Project project, SensorContext context) {
    files.add(file);
  }
  
  public List<InputFile> getParsedFilesList() {
    return files;
  }
  

}

public class XamlFileSensorTest {

  private static final int PARSED_FILE_COUNT = 1;
  
  private XamlFileSensorImpl sensor;
  private Project project;
  
  @Before
  public void setup() {
    sensor = new XamlFileSensorImpl();
    project = XamlTestUtils.mockProject();
  }
  
  @Test
  public void shouldExecuteOnProjectTest() {
    assertTrue(sensor.shouldExecuteOnProject(project));
  }
  
  @Test
  public void analyseTest() {
    sensor.analyse(project, null);
    assertEquals(PARSED_FILE_COUNT, sensor.getParsedFilesList().size());
  }
  
  
}
