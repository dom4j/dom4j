/*
 * Copyright 2001-2004 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id: Timer.java,v 1.4 2005/01/29 14:53:13 maartenc Exp $
 */

package org.dom4j.samples.performance;

/**
 * A timer for use in performance monitoring
 * 
 * @author <a href="mailto:james.strachan@metastuff.com">James Strachan </a>
 * @version $Revision: 1.4 $
 */
public class Timer {

    /** Whether the performance of each run is printed */
    protected static boolean VERBOSE = false;

    /** Default number of loops */
    protected static final int DEFAULT_LOOP_COUNT = 40;

    /** The number of the first loops to display */
    private int displayCount = 4;

    /** Number of loops to perform */
    private int loopCount = DEFAULT_LOOP_COUNT;

    private Task task;

    public Timer() {
    }

    public Timer(Task task) {
        this.task = task;
    }

    /**
     * Performs a piece of code a number of times in a loop
     * 
     * @param loopCount
     *            is the number of loops to perform
     */
    public void run() throws Exception {
        Task task = getTask();
        int size = getLoopCount();
        if (size <= 0 || task == null) {
            return;
        }

        long[] times = new long[size];
        for (int i = 0; i < size; i++) {
            long start = System.currentTimeMillis();

            task.run();

            long end = System.currentTimeMillis();
            times[i] = end - start;
        }

        printSummary(times);
    }

    // Properties
    // -------------------------------------------------------------------------
    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public int getLoopCount() {
        return loopCount;
    }

    public void setLoopCount(int loopCount) {
        this.loopCount = loopCount;
    }

    // Implementation methods
    // -------------------------------------------------------------------------
    protected void printSummary(long[] times) {
        println("Performance summary");

        println("Number of runs: " + loopCount);

        if (VERBOSE || loopCount < displayCount) {
            displayCount = loopCount;
        }
        for (int i = 0; i < displayCount; i++) {
            println("run: " + i + " took: " + times[i] + " (ms)");
        }

        long minimum = times[0];
        for (int i = 1; i < loopCount; i++) {
            long time = times[i];
            if (time < minimum) {
                minimum = time;
            }
        }

        println("Minimum time of run                   : " + minimum + " (ms)");

        // average ignoring first loop
        long total = 0;
        for (int i = 0; i < loopCount; i++) {
            total += times[i];
        }

        double average = total / loopCount;

        println("Average time of run                   : " + average + " (ms)");

        if (loopCount == 1) {
            return;
        }
        long total_1 = total - times[0];
        average = total_1 / (loopCount - 1);

        println("Average (excluding first run)         : " + average + " (ms)");

        if (loopCount == 2) {
            return;
        }
        long total_2 = total_1 - times[1];
        average = total_2 / (loopCount - 2);

        println("Average (excluding first & second run): " + average + " (ms)");

        println("Total time of run                     : " + total + " (ms)");
        println("Total (excluding first run)           : " + total_1 + " (ms)");
        println("Total (excluding first & second run)  : " + total_2 + " (ms)");

        return;
    }

    protected void println(String text) {
        System.out.println(text);
    }
}

/*
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided that the
 * following conditions are met:
 * 
 * 1. Redistributions of source code must retain copyright statements and
 * notices. Redistributions must also contain a copy of this document.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * 
 * 3. The name "DOM4J" must not be used to endorse or promote products derived
 * from this Software without prior written permission of MetaStuff, Ltd. For
 * written permission, please contact dom4j-info@metastuff.com.
 * 
 * 4. Products derived from this Software may not be called "DOM4J" nor may
 * "DOM4J" appear in their names without prior written permission of MetaStuff,
 * Ltd. DOM4J is a registered trademark of MetaStuff, Ltd.
 * 
 * 5. Due credit should be given to the DOM4J Project - http://www.dom4j.org
 * 
 * THIS SOFTWARE IS PROVIDED BY METASTUFF, LTD. AND CONTRIBUTORS ``AS IS'' AND
 * ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL METASTUFF, LTD. OR ITS CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 * Copyright 2001-2004 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * $Id: Timer.java,v 1.4 2005/01/29 14:53:13 maartenc Exp $
 */
