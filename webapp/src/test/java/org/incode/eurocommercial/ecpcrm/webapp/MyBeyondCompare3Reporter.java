package org.incode.eurocommercial.ecpcrm.webapp;

import java.text.MessageFormat;

import org.approvaltests.reporters.GenericDiffReporter;

public class MyBeyondCompare3Reporter extends GenericDiffReporter
{
    public static final MyBeyondCompare3Reporter INSTANCE     = new MyBeyondCompare3Reporter();
    static final String DIFF_PROGRAM = "C:\\Program Files (x86)\\Beyond Compare 3\\BCompare.exe";

    static final String MESSAGE      = MessageFormat.format("Unable to find Beyond Compare at {0}", DIFF_PROGRAM);

    public MyBeyondCompare3Reporter()
    {
        super(DIFF_PROGRAM, MESSAGE);
    }
}
