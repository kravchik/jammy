package yk.jcommon.jammy;

import org.junit.Test;
import yk.ycollections.YArrayList;

import static yk.jammy.JammyShortNames.objNamed;
import static yk.jcommon.jammy.JammyTestUtils.test;
import static yk.ycollections.YArrayList.al;

/**
 * 12.03.2024
 */
public class TestMatchClass {
    @Test
    public void test1() {
        test("[{}]", al(al()), al(objNamed(YArrayList.class)));
    }
}
