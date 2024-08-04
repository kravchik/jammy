package yk.jcommon.jammy;

import yk.jammy.JammyMatcher;
import yk.ycollections.YList;
import yk.ycollections.YMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * 01.08.2024
 */
public class JammyTestUtils {
    public static void test(String expected, Object data, Object pattern) {
        YList<YMap<String, Object>> actual = new JammyMatcher().match(data, pattern);
        if (expected == null) assertNull(actual);
        else assertEquals(expected, actual + "");
    }
}
