/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2015, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.jbossset.bugclerk.cli;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.jboss.jbossset.bugclerk.BugClerk;
import org.jboss.jbossset.bugclerk.BugclerkConfiguration;
import org.jboss.jbossset.bugclerk.aphrodite.AphroditeClient;

public final class BugClerkCLI {

    private static final int PROGRAM_THROWN_EXCEPTION = 3;

    private BugClerkCLI() {
    }

    public static void main(String[] args) {
        try {
            BugClerkArguments arguments = CommandLineInterfaceUtils.extractParameters(new BugClerkArguments(), args);
            if (arguments.getIds().isEmpty())
                throw new IllegalArgumentException("No IDs provided.");

            AphroditeClient aphrodite = new AphroditeClient();
            arguments.setIssues(aphrodite.loadIssues(arguments.getIds()));
            BugClerkArguments validatedArgs = BugClerkArguments.validateArgs(arguments);
            new BugClerk(aphrodite, BugClerkCLI.instantiateConfiguration(validatedArgs))
            .runAndReturnsViolations(validatedArgs.getIssues());
            aphrodite.close();
        } catch (Throwable t) {
            System.out.println(t.getMessage());
            if (t.getCause() != null)
                System.out.println(t.getCause().getMessage());
            System.exit(PROGRAM_THROWN_EXCEPTION);
        }
    }

    private static Collection<String> extractChecknamesFromArgs(CommonArguments arguments) {
        return ! "".equals(arguments.getChecknames()) ? Arrays.asList(arguments.getChecknames().split(",")) : new ArrayList<String>(0);
    }

    public static BugclerkConfiguration instantiateConfiguration(CommonArguments arguments) {
        BugclerkConfiguration configuration = new BugclerkConfiguration();
        configuration.setDebug(arguments.isDebug());
        configuration.setFailOnViolation(arguments.isFailOnViolation());
        configuration.setHtmlReportFilename(arguments.getHtmlReportFilename());
        configuration.setXmlReportFilename(arguments.getXmlReportFilename());
        configuration.setReportViolation(arguments.isReportToBz());
        configuration.setChecknames(extractChecknamesFromArgs(arguments));
        return configuration;
    }
}
