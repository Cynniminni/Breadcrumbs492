package com.tumblr.breadcrumbs492.testapplication;

import android.test.ActivityInstrumentationTestCase2;
import android.test.ActivityTestCase;
import android.test.InstrumentationTestCase;

/**
 * Created by scott on 3/19/2015.
 */
public class ActivityTest extends InstrumentationTestCase {
    public void test() throws Exception {
        final int expected = 1;
        final int reality = 5;
        assertEquals(expected, reality);
    }

}

