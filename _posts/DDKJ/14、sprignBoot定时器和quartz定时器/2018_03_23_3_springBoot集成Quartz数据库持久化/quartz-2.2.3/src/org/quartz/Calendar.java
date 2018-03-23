
/* 
 * Copyright 2001-2009 Terracotta, Inc. 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not 
 * use this file except in compliance with the License. You may obtain a copy 
 * of the License at 
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0 
 *   
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT 
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the 
 * License for the specific language governing permissions and limitations 
 * under the License.
 * 
 */

package org.quartz;

/**
 * An interface to be implemented by objects that define spaces of time during 
 * which an associated <code>{@link Trigger}</code> may (not) fire. Calendars 
 * do not define actual fire times, but rather are used to limit a 
 * <code>Trigger</code> from firing on its normal schedule if necessary. Most 
 * Calendars include all times by default and allow the user to specify times 
 * to exclude. 
 * 
 * <p>As such, it is often useful to think of Calendars as being used to <I>exclude</I> a block
 * of time - as opposed to <I>include</I> a block of time. (i.e. the 
 * schedule &quot;fire every five minutes except on Sundays&quot; could be 
 * implemented with a <code>SimpleTrigger</code> and a 
 * <code>WeeklyCalendar</code> which excludes Sundays)</p>
 * 
 * <p>Implementations MUST take care of being properly <code>Cloneable</code> 
 * and <code>Serializable</code>.</p>
 * 
 * @author James House
 * @author Juergen Donnerstag
 */
public interface Calendar extends java.io.Serializable, java.lang.Cloneable {

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Constants.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    int MONTH = 0;

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Interface.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    /**
     * <p>
     * Set a new base calendar or remove the existing one.
     * </p>
     */
    void setBaseCalendar(Calendar baseCalendar);

    /**
     * <p>
     * Get the base calendar. Will be null, if not set.
     * </p>
     */
    Calendar getBaseCalendar();

    /**
     * <p>
     * Determine whether the given time (in milliseconds) is 'included' by the
     * Calendar.
     * </p>
     */
    boolean isTimeIncluded(long timeStamp);

    /**
     * <p>
     * Determine the next time (in milliseconds) that is 'included' by the
     * Calendar after the given time.
     * </p>
     */
    long getNextIncludedTime(long timeStamp);

    /**
     * <p>
     * Return the description given to the <code>Calendar</code> instance by
     * its creator (if any).
     * </p>
     * 
     * @return null if no description was set.
     */
    String getDescription();

    /**
     * <p>
     * Set a description for the <code>Calendar</code> instance - may be
     * useful for remembering/displaying the purpose of the calendar, though
     * the description has no meaning to Quartz.
     * </p>
     */
    void setDescription(String description);
    
    Object clone();
}
