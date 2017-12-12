package org.jboss.jbossset.bugclerk.checks.utils;

import static org.jboss.jbossset.bugclerk.checks.utils.CollectionUtils.asSetOf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jboss.jbossset.bugclerk.Candidate;
import org.jboss.jbossset.bugclerk.MockUtils;
import org.jboss.set.aphrodite.domain.Comment;
import org.jboss.set.aphrodite.domain.Issue;
import org.mockito.Mockito;

public final class BugClerkMockingHelper {

    public static final String DEV_ACK_FLAG = "devel_ack";
    public static final String QA_ACK_FLAG = "qa_ack";
    public static final String PM_ACK_FLAG = "pm_ack";

    private BugClerkMockingHelper() {
    }

    public static Collection<Candidate> buildTestSubjectWithComments(Issue mock, String... commentsContent) {
        List<Comment> comments = new ArrayList<Comment>();
        for (String comment : commentsContent)
            comments.add(MockUtils.mockComment("0", comment, mock.getTrackerId().get()));
        Mockito.when(mock.getComments()).thenReturn(comments);
        return asSetOf(new Candidate(mock));
    }

    public static Collection<Candidate> buildTestSubjectWithComment(Issue mock, String comment) {
        List<Comment> comments = new ArrayList<Comment>();
        comments.add(MockUtils.mockComment("0", comment, mock.getTrackerId().get()));
        Mockito.when(mock.getComments()).thenReturn(comments);
        return CollectionUtils.asSetOf(new Candidate(mock));
    }

    public static Collection<Candidate> buildTestSubjectWithComment(String bugId, String comment) {
        List<Comment> comments = new ArrayList<Comment>();
        comments.add(MockUtils.mockComment("0", comment, bugId));
        Issue mock = MockUtils.mockBzIssue(bugId, "summary");
        Mockito.when(mock.getComments()).thenReturn(comments);
        return CollectionUtils.asSetOf(new Candidate(mock));
    }
    /*
     * public static Collection<Candidate> filterCandidateOut(Collection<Candidate> candidates) { for ( Candidate candidate :
     * candidates ) candidate.setFiltered(true); return candidates; }
     * 
     * public static List<Flag> createFlag(String flagname, Flag.Status status) { List<Flag> flags = new ArrayList<Flag>(1);
     * flags.add(new Flag(flagname, "setter?", status)); return flags; }
     * 
     * public static List<Flag> createAllThreeFlagsAs(Flag.Status status) { List<Flag> flags = new ArrayList<Flag>(3);
     * flags.add(new Flag(QA_ACK_FLAG, "setter?", status)); flags.add(new Flag(PM_ACK_FLAG, "setter?", status)); flags.add(new
     * Flag(DEV_ACK_FLAG, "setter?", status)); return flags; }
     * 
     * public static Collection<Candidate> buildTestSubjectWithComments(int bugId, String... commentsContent) {
     * SortedSet<Comment> comments = new TreeSet<Comment>(); for ( String comment : commentsContent )
     * comments.add(MockUtils.mockComment(0, comment, bugId)); return asSetOf(new Candidate(MockUtils.mockBug(bugId, "summary"),
     * comments)); }
     * 
     * public static Collection<Candidate> buildTestSubjectWithComment(int bugId, String comment) { SortedSet<Comment> comments
     * = new TreeSet<Comment>(); comments.add(MockUtils.mockComment(0, comment, bugId)); return CollectionUtils.asSetOf(new
     * Candidate(MockUtils.mockBug(bugId, "summary"), comments)); }
     * 
     * public static Collection<Candidate> buildTestSubjectWithComment(Bug mock) { return CollectionUtils.asSetOf(new
     * Candidate(mock, new TreeSet<Comment>())); }
     */
}
