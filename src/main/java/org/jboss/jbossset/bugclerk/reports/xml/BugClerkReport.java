package org.jboss.jbossset.bugclerk.reports.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class BugClerkReport {

    private List<BugReport> bugs;

    /**
     * Empty constructor is required by JAX-B
     */
    public BugClerkReport() { //NOPMD
    }

    public BugClerkReport(List<BugReport> reports) {
        this.bugs = reports;
    }

    @XmlElement(name = "bz")
    public List<BugReport> getBugs() {
        return bugs;
    }

    public void setBugs(List<BugReport> bugs) {
        this.bugs = bugs;
    }

}
