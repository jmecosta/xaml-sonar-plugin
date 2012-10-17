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

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang.StringUtils;
import org.jfree.util.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.resources.InputFile;
import org.sonar.api.resources.Project;

/**
 * Count lines of code in XML files.
 *
 * @author Matthijs Galesloot
 */
public final class XamlLineCouterSensor implements Sensor {

    private static final Logger LOG = LoggerFactory.getLogger(XamlLineCouterSensor.class);

    private void addMeasures(SensorContext sensorContext, File file, org.sonar.api.resources.File xmlFile) {

        LineIterator iterator = null;
        int numLines = 0;
        int numEmptyLines = 0;

        try {
            iterator = FileUtils.lineIterator(file);

            while (iterator.hasNext()) {
                String line = iterator.nextLine();
                numLines++;
                if (StringUtils.isEmpty(line)) {
                    numEmptyLines++;
                }
            }
        } catch (IOException e) {
            LOG.warn(e.getMessage());
        } finally {
            LineIterator.closeQuietly(iterator);
        }

        try {

            Log.debug("Count comment in " + file.getPath());

            int numCommentLines = new XamlLineCountParser().countLinesOfComment(FileUtils.openInputStream(file));
            sensorContext.saveMeasure(xmlFile, CoreMetrics.LINES, (double) numLines);
            sensorContext.saveMeasure(xmlFile, CoreMetrics.COMMENT_LINES, (double) numCommentLines);
            sensorContext.saveMeasure(xmlFile, CoreMetrics.NCLOC, (double) numLines - numEmptyLines - numCommentLines);
        } catch (Exception e) {
            LOG.debug("Fail to count lines in " + file.getPath(), e);
        }

        LOG.debug("LineCountSensor: " + xmlFile.getKey() + ":" + numLines + "," + numEmptyLines + "," + 0);
    }

    public void analyse(Project project, SensorContext sensorContext) {

        List<InputFile> sourceFiles = project.getFileSystem().mainFiles(XamlLanguage.KEY);

        for (InputFile inputFile : sourceFiles) {
            org.sonar.api.resources.File htmlFile = org.sonar.api.resources.File.fromIOFile(inputFile.getFile(), project);
            addMeasures(sensorContext, inputFile.getFile(), htmlFile);
        }

    }

    /**
     * This sensor only executes on XML projects.
     */
    public boolean shouldExecuteOnProject(Project project) {
        return XamlLanguage.KEY.equals(project.getLanguageKey());
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}