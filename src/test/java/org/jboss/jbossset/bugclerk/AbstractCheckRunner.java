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
package org.jboss.jbossset.bugclerk;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.jboss.pull.shared.connectors.bugzilla.Bug;
import org.jboss.pull.shared.connectors.bugzilla.Comment;
import org.jboss.pull.shared.connectors.common.Flag;
import org.junit.After;
import org.junit.Before;
import org.mockito.MockitoAnnotations;

public abstract class AbstractCheckRunner {

    protected RuleEngine engine;
    protected final String checkName;

    protected static final String DEV_ACK_FLAG = "devel_ack";
    protected static final String QA_ACK_FLAG = "qa_ack";
    protected static final String PM_ACK_FLAG = "pm_ack";

    protected static final SortedSet<Comment> NO_COMMENTS = new TreeSet<Comment>();

    public AbstractCheckRunner() {
        checkName = this.getClass().getSimpleName();
    }

    @Before
    public void initRuleEngine() {
        this.engine = new RuleEngine(BugClerk.KIE_SESSION);
    }

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void shutdownRuleEngine() {
        this.engine.shutdownRuleEngine();
    }

    protected Bug createMockedBug(int bugId) {
        return testSpecificStubbingForBug(MockUtils.mockBug(bugId, "summary"));
    }

    protected Bug testSpecificStubbingForBug(Bug bug) {
        return bug;
    }

    protected Comment testSpecificStubbingForComment(Comment comment) {
        return comment;
    }

    protected Comment createMockedComment(int id, String text, int bugId) {
        return testSpecificStubbingForComment(MockUtils.mockComment(id, text, bugId));
    }

    protected Collection<Candidate> buildTestSubjectWithComment(int bugId, String comment) {
        SortedSet<Comment> comments = new TreeSet<Comment>();
        comments.add(createMockedComment(0, comment, bugId));
        return createListForOneCandidate(new Candidate(createMockedBug(bugId), comments));
    }

    protected Collection<Candidate> buildTestSubjectWithComments(int bugId, String... commentsContent) {
        SortedSet<Comment> comments = new TreeSet<Comment>();
        for ( String comment : commentsContent )
            comments.add(createMockedComment(0, comment, bugId));
        return createListForOneCandidate(new Candidate(createMockedBug(bugId), comments));
    }

    protected Collection<Candidate> createListForOneCandidate(Candidate candidate) {
        Collection<Candidate> candidates = new ArrayList<Candidate>(1);
        candidates.add(candidate);
        return candidates;
    }

    protected Collection<Candidate> buildTestSubject(int bugId) {
        return createListForOneCandidate ( new Candidate(createMockedBug(bugId), new TreeSet<Comment>() ));
    }

    protected Collection<Candidate> filterCandidateOut(Collection<Candidate> candidates) {
        for ( Candidate candidate : candidates ) {
            candidate.setFiltered(true);
        }
        return candidates;
    }

    protected void assertResultsIsAsExpected(Collection<Violation> violations, String checkname, int bugId) {
        assertThat(violations.size(), is(1));
        for ( Violation v : violations ) {
            assertThat(v.getBug().getId(), is(bugId));
            assertThat(v.getCheckName(), is(checkname));
        }
    }

    protected void assertResultsIsAsExpected(Collection<Violation> violations, String checkname, int bugId, int nbViolationExpected) {
        assertThat(violations.size(), is(nbViolationExpected));
        for ( Violation v : violations ) {
            assertThat(v.getBug().getId(), is(bugId));
            assertThat(v.getCheckName(), is(checkname));
        }
    }

    protected List<Flag> createAllThreeFlagsAs(Flag.Status status) {
        List<Flag> flags = new ArrayList<Flag>(3);
        flags.add(new Flag(QA_ACK_FLAG, "setter?", status));
        flags.add(new Flag(PM_ACK_FLAG, "setter?", status));
        flags.add(new Flag(DEV_ACK_FLAG, "setter?", status));
        return flags;
    }
}