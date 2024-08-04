package yk.jcommon.jammy;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static yk.jammy.JammyShortNames.i;
import static yk.jammy.JammyShortNames.var;
import static yk.jcommon.jammy.JammyTestUtils.test;
import static yk.ycollections.YArrayList.al;
import static yk.ycollections.YHashSet.hs;

/**
 * Created by Yuri Kravchik on 05.01.2019
 */
public class TestJammyVar {

    @Test
    public void testVar() {
        test("[{x=a, y=c}]", al("a", "a", "c"), al(var("x"), var("x"), var("y")));
        test("[{x=[_a], y=c}]", al(al("_a"), al("_a"), "c"), al(var("x", al("_a")), var("x"), var("y")));
        test("null", al(al("_a"), al("_a"), "c"), al(var("x", al("_a")), var("x", al("_b")), var("y")));
        test("null", al(hs("_a", "_b"), hs("_a"), "c"), al(var("x", hs("_a")), var("x"), var("y")));
        test("null", al(hs("_a", "_b"), hs("_a", "_b"), "c"), al(var("x", hs("_a")), var("x"), var("y")));
        //not required, it is better to place a pattern in only one var, though, currently, it is logically better at least
        test("null", al(hs("_a", "_b"), hs("_a", "_b"), "c"), al(var("x", hs("_a")), var("x", hs("_b")), var("y")));
        test("[{x=null}]", al((Object) null), al(var("x")));
        test("[{x=null}, {x=hello}]", al((Object) null, "hello"), i(var("x")));
        test("[{x=null}]", al(null, null), al(var("x"), var("x")));
    }

}