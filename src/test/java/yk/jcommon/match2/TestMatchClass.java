package yk.jcommon.match2;

import org.junit.Test;
import yk.ycollections.YArrayList;

import static org.junit.Assert.assertEquals;
import static yk.jcommon.match2.MatcherShortNames.objNamed;
import static yk.ycollections.YArrayList.al;
import static yk.ycollections.YHashMap.hm;
import static yk.ycollections.YHashSet.hs;

/**
 * 12.03.2024
 */
public class TestMatchClass {
    @Test
    public void test1() {
        assertEquals(hs(hm()), new Matcher().match(al(al()), al(objNamed(YArrayList.class))));
        assertEquals(hs(hm()), new Matcher().match(al(al()), al(objNamed(YArrayList.class))));
    }
}
