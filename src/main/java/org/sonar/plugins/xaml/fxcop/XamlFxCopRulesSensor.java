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

import java.io.*;
import java.nio.charset.Charset;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.stax2.XMLInputFactory2;
import org.codehaus.staxmate.SMInputFactory;
import org.codehaus.staxmate.in.SMEvent;
import org.codehaus.staxmate.in.SMHierarchicCursor;
import org.codehaus.staxmate.in.SMInputCursor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.resources.Project;
import org.sonar.api.resources.Resource;
import org.sonar.api.rules.Rule;
import org.sonar.api.rules.RuleFinder;
import org.sonar.api.rules.RuleQuery;
import org.sonar.api.rules.Violation;
import org.sonar.api.utils.SonarException;
import org.sonar.plugins.xaml.utils.XamlReportSensor;

/**
 * Custom Rule Import, all static analysis are supported.
 *
 *
 * @author jorge costa
 * @author jorge costa @todo enable include dirs (-I) @todo allow configuration
 * of path to analyze
 */
public class XamlFxCopRulesSensor extends XamlReportSensor {

    public static final String REPORT_PATH_KEY = "sonar.xaml.fxcop.reportPath";
    private static final String DEFAULT_REPORT_PATH = "fxcop-reports/fxcop-result-*.xml";
    private RulesProfile profile;
    private static final Logger LOG = LoggerFactory.getLogger(XamlFxCopRulesSensor.class);
    private static final String NAMESPACE = "Namespace";
    private static final String NAMESPACES = "Namespaces";
    private static final String TARGETS = "Targets";
    private static final String MESSAGE = "Message";
    private static final String MESSAGES = "Messages";
    private static final String MODULE = "Module";
    private static final String TYPENAME = "TypeName";
    private static final String LINE = "Line";
    private Project project;
    private SensorContext context;
    private RuleFinder ruleFinder;
    private String repositoryKey;

    /**
     * {@inheritDoc}
     */
    public XamlFxCopRulesSensor(RuleFinder ruleFinder, Configuration conf,
            RulesProfile profile) {
        super(ruleFinder, conf);
        this.profile = profile;
        this.ruleFinder = ruleFinder;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean shouldExecuteOnProject(Project project) {
        return super.shouldExecuteOnProject(project)
                && !profile.getActiveRulesByRepository(XamlFxCopRulesRepository.REPOSITORY_KEY).isEmpty();
    }

    @Override
    protected String reportPathKey() {
        return REPORT_PATH_KEY;
    }

    @Override
    protected String defaultReportPath() {
        return DEFAULT_REPORT_PATH;
    }

    @Override
    protected void processReport(final Project project, final SensorContext context, File report)
            throws javax.xml.stream.XMLStreamException, UnsupportedEncodingException {
        
        this.project = project;
        this.context = context;
        parse(report);
    }

      protected SMInputFactory initStax() {
    XMLInputFactory xmlFactory = XMLInputFactory2.newInstance();
    xmlFactory.setProperty(XMLInputFactory.IS_COALESCING, Boolean.TRUE);
    xmlFactory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, Boolean.FALSE);
    xmlFactory.setProperty(XMLInputFactory.SUPPORT_DTD, Boolean.FALSE);
    xmlFactory.setProperty(XMLInputFactory.IS_VALIDATING, Boolean.FALSE);
    return new SMInputFactory(xmlFactory);
  }
    /**
     * Parses a processed violation file.
     *
     * @param file the file to parse
     */
    public void parse(File file) throws UnsupportedEncodingException {

        this.repositoryKey = XamlFxCopRulesRepository.REPOSITORY_KEY;

        SMInputFactory inputFactory = initStax();
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            SMHierarchicCursor cursor = inputFactory.rootElementCursor(new InputStreamReader(fileInputStream, Charset.forName("UTF-8")));
            SMInputCursor mainCursor = cursor.advance().childElementCursor();
            while (mainCursor.getNext() != null) {
                if (NAMESPACES.equals(mainCursor.getQName().getLocalPart())) {
                    parseNamespacesBloc(mainCursor);
                } else if (TARGETS.equals(mainCursor.getQName().getLocalPart())) {
                    parseTargetsBloc(mainCursor);
                }
            }
            cursor.getStreamReader().closeCompletely();
        } catch (XMLStreamException e) {
            throw new SonarException("Error while reading FxCop result file: " + file.getAbsolutePath(), e);
        } catch (FileNotFoundException e) {
            throw new SonarException("Cannot find FxCop result file: " + file.getAbsolutePath(), e);
        } finally {
            IOUtils.closeQuietly(fileInputStream);
        }
    }

    private void parseNamespacesBloc(SMInputCursor cursor) throws XMLStreamException {
        // Cursor in on <Namespaces>
        SMInputCursor namespacesCursor = cursor.childElementCursor(NAMESPACE);
        while (namespacesCursor.getNext() != null) {
            SMInputCursor messagesCursor = namespacesCursor.descendantElementCursor(MESSAGE);
            while (messagesCursor.getNext() != null) {
                createViolationFromMessageAtProjectLevel(messagesCursor);
            }
        }
    }

    private void parseTargetsBloc(SMInputCursor cursor) throws XMLStreamException {
        // Cursor on <Targets>
        SMInputCursor modulesCursor = cursor.descendantElementCursor(MODULE);
        while (modulesCursor.getNext() != null) {
            parseModuleMessagesBloc(modulesCursor);
        }
    }

    private void parseModuleMessagesBloc(SMInputCursor cursor) throws XMLStreamException {
        // Cursor on <Module>
        SMInputCursor moduleChildrenCursor = cursor.childElementCursor();
        while (moduleChildrenCursor.getNext() != null) {
            if (MESSAGES.equals(moduleChildrenCursor.getQName().getLocalPart())) {
                // We are on <Messages>, look for <Message>
                SMInputCursor messagesCursor = moduleChildrenCursor.childElementCursor(MESSAGE);
                while (messagesCursor.getNext() != null) {
                    createViolationFromMessageAtProjectLevel(messagesCursor);
                }
            } else if (NAMESPACES.equals(moduleChildrenCursor.getQName().getLocalPart())) {
                // We are on <Namespaces>, get <Namespace>
                SMInputCursor namespaceCursor = moduleChildrenCursor.childElementCursor();
                while (namespaceCursor.getNext() != null) {
                    SMInputCursor typeCursor = namespaceCursor.childElementCursor().advance().childElementCursor();
                    while (typeCursor.getNext() != null) {
                        parseTypeBloc(typeCursor);
                    }
                }
            }
        }
    }

    private void parseTypeBloc(SMInputCursor cursor) throws XMLStreamException {
        // Cursor on <Type>

        SMInputCursor messagesCursor = cursor.descendantElementCursor(MESSAGE);
        while (messagesCursor.getNext() != null) {
            // Cursor on <Message>
            if (messagesCursor.getCurrEvent() == SMEvent.START_ELEMENT) {

                Rule currentRule = ruleFinder.find(RuleQuery.create().withRepositoryKey(repositoryKey).withKey(messagesCursor.getAttrValue(TYPENAME)));
                if (currentRule != null) {
                    // look for all potential issues
                    searchForViolations(messagesCursor, currentRule);
                } else {
                    LOG.warn("Could not find the following rule in the FxCop rule repository: " + messagesCursor.getAttrValue(TYPENAME));
                }

            }
        }
    }

    protected void searchForViolations(SMInputCursor messagesCursor, Rule currentRule) throws XMLStreamException {
        SMInputCursor issueCursor = messagesCursor.childElementCursor();
        while (issueCursor.getNext() != null) {
            final Resource<?> resource;
            String path = issueCursor.getAttrValue("Path");
            String file = issueCursor.getAttrValue("File");
            if (StringUtils.isNotEmpty(path) && StringUtils.isNotEmpty(file)) {
                File sourceFile = new File(path, file).getAbsoluteFile();
                

                resource = org.sonar.api.resources.File.fromIOFile(sourceFile, project);
                if(context.getResource(resource) != null){
                    // Cursor on Issue
                    Violation violation = Violation.create(currentRule, resource);
                    String lineNumber = issueCursor.getAttrValue(LINE);
                    if (lineNumber != null) {
                        violation.setLineId(Integer.parseInt(lineNumber));
                    }
                    violation.setMessage(issueCursor.collectDescendantText().trim());
                    violation.setSeverity(currentRule.getSeverity());
                    context.saveViolation(violation);
                }
            }
        }
    }
    
    private void createViolationFromMessageAtProjectLevel(SMInputCursor messagesCursor) throws XMLStreamException {
        Rule currentRule = ruleFinder.find(RuleQuery.create().withRepositoryKey(repositoryKey).withKey(messagesCursor.getAttrValue(TYPENAME)));
        if (currentRule != null) {
            // the violation is saved at project level, not on a specific resource
            Violation violation = Violation.create(currentRule, project);
            violation.setMessage(messagesCursor.collectDescendantText().trim());
            violation.setSeverity(currentRule.getSeverity());
            context.saveViolation(violation);
            LOG.debug("Save violation: " + violation.getRule());
        } else {
            LOG.debug("Could not find the following rule in the FxCop rule repository: " + messagesCursor.getAttrValue(TYPENAME));
        }
    }
}
