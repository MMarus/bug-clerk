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
package org.jboss.jbossset.bugclerk.checks;

import static org.jboss.jbossset.bugclerk.checks.utils.AssertsHelper.assertResultsIsAsExpected;

import java.util.Optional;

import org.jboss.jbossset.bugclerk.AbstractCheckRunner;
import org.jboss.jbossset.bugclerk.Candidate;
import org.jboss.jbossset.bugclerk.MockUtils;
import org.jboss.jbossset.bugclerk.checks.utils.CollectionUtils;
import org.jboss.jbossset.bugclerk.checks.utils.DateUtils;
import org.jboss.set.aphrodite.domain.Issue;
import org.jboss.set.aphrodite.domain.IssuePriority;
import org.jboss.set.aphrodite.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class HighPriorityIssueNotAssigned extends AbstractCheckRunner {

    private Issue mock;
    private final String bugId = "143794";

    @Before
    public void prepareBugMock() {
        mock = MockUtils.mockBug(bugId, "summary");
        Mockito.when(mock.getPriority()).thenReturn(IssuePriority.CRITICAL);        
        Mockito.when(mock.getAssignee()).thenReturn(Optional.empty());
        Mockito.when(mock.getCreationTime()).thenReturn(Optional.of(DateUtils.threeWeeksAgo()));
    }

    @Test
    public void violationIssueOverTwoWeeksWithHighPriority() {
        assertResultsIsAsExpected(
                engine.runCheckOnBugs(CollectionUtils.asSetOf(new Candidate(mock)), checkName), checkName,bugId);
    }

    @Test
    public void noViolationIssueLowerThanHighPriority() {
        Mockito.when(mock.getPriority()).thenReturn(IssuePriority.MINOR);
        assertResultsIsAsExpected(engine.runCheckOnBugs(CollectionUtils.asSetOf(new Candidate(mock)), checkName), checkName,
                bugId, 0);
    }
    
    @Test
    public void noViolationIssueHighPriorityButLessThanTwoWeeks() {
        Mockito.when(mock.getCreationTime()).thenReturn(Optional.of(DateUtils.lastWeek()));
        System.out.println(mock.getCreationTime().get());
        assertResultsIsAsExpected(engine.runCheckOnBugs(CollectionUtils.asSetOf(new Candidate(mock)), checkName), checkName,
                bugId, 0);
    }
    
    @Test
    public void noViolationIfIssueAssigned() {
        Mockito.when(mock.getAssignee()).thenReturn(Optional.of(new User("rpelisse@redhat.com", "Romain Pelisse")));
        assertResultsIsAsExpected(engine.runCheckOnBugs(CollectionUtils.asSetOf(new Candidate(mock)), checkName), checkName,
                bugId, 0);
    }
}
