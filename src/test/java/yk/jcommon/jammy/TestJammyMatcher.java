package yk.jcommon.jammy;

import org.junit.Ignore;
import org.junit.Test;
import yk.jammy.*;

import static junit.framework.TestCase.assertEquals;
import static yk.jammy.JammyShortNames.*;
import static yk.jcommon.jammy.JammyTestUtils.test;
import static yk.ycollections.YArrayList.al;
import static yk.ycollections.YHashMap.hm;
import static yk.ycollections.YHashSet.hs;

/**
 * Created with IntelliJ IDEA.
 * User: yuri
 * Date: 01/11/15
 * Time: 17:02
 */
public class TestJammyMatcher {
    //todo test deeper with 0 deepness

    @Test
    public void test1() {
        assertEquals(al(hm()), new JammyMatcher().match(al("a", "b"), i("a")));
        assertEquals(al(hm("index", 0)), new JammyMatcher().match(al("a", "b"), i(var("index"), "a")));
        assertEquals(al(hm("$a", "a"), hm("$a", "b")), new JammyMatcher().match(al("a", "b"), i(var("$a"))));
        assertEquals(al(hm("$a", "a"), hm("$a", "b")), new JammyMatcher().match(al("a", "b"), new JammyAnd(i(var("$a")))));
        assertEquals(al(hm("$a", "a"), hm("$a", "b")), new JammyMatcher().match(al("a", "b"), new JammyAnd(i("a"), i(var("$a")))));
        assertEquals(al(hm("$a", "a", "iFirst", 0, "iSecond", 0), hm("$a", "b", "iFirst", 0, "iSecond", 1)),
                new JammyMatcher().match(al("a", "b"), new JammyAnd(i(var("iFirst"), "a"), i(var("iSecond"), var("$a")))));
    }

    @Test
    public void testList() {
        test("[{}]", al(), al());
        test("[{}]", al("a"), al("a"));
        test("null", al("a"), al());
        test("null", al(), al("a"));
        test("null", "", al("a"));

        test("[{$b=b}]", al("a", "b"), al("a", var("$b")));
        test("null", al("a", "b"), al(var("$b"), var("$b")));
        test("[{$a=a, $b=b}]", al("a", "b"), al(var("$a"), var("$b")));

    }

    @Test
    public void testMap() {
        test("[{}]", hm(), hm());
        test("[{}]", hm("ka", "va"), hm("ka", "va"));
        test("[{}]", hm("ka", "va"), hm());
        test("null", hm(), hm("ka", "va"));
        test("null", "", hm("ka", "va"));

        test("[{$va=va}]", hm("ka", "va", "kb", "vb"), hm("ka", var("$va")));
        test("[]", hm("ka", "va", "kb", "vb"), hm("ka", var("$va"), "kb", var("$va")));
        test("[{$va=va, $vb=vb}]", hm("ka", "va", "kb", "vb"), hm("ka", var("$va"), "kb", var("$vb")));

    }

    @Test
    public void testMapFullMatcher() {
        test("[{$other={}}]", hm(), new JammyMap(var("$other"), hm()));
        test("[{$other={}}]", hm("ka", "va"), new JammyMap(var("$other"), hm("ka", "va")));
        test("null", hm(), new JammyMap(var("$other"), hm("ka", "va")));
        test("null", "", new JammyMap(var("$other"), hm("ka", "va")));

        test("[{$va=va, $other={kb=vb}}]", hm("ka", "va", "kb", "vb"), new JammyMap(var("$other"), hm("ka", var("$va"))));
        test("[{$va=va, $vb=vb, $other={}}]", hm("ka", "va", "kb", "vb"), new JammyMap(var("$other"), hm("ka", var("$va"), "kb", var("$vb"))));

        test("[{$other={}, $va=va, $vb=vb}]", al(hm(), hm("ka", "va", "kb", "vb")), al(var("$other"), new JammyMap(var("$other"), hm("ka", var("$va"), "kb", var("$vb")))));
        test("[{$va=va, $vb=vb, $other={}}]", al(hm("ka", "va", "kb", "vb"), hm()), al(new JammyMap(var("$other"), hm("ka", var("$va"), "kb", var("$vb"))), var("$other")));

        test("null", al(hm("ka", "va", "kb", "vb"), hm()), al(new JammyMap(var("$other"), hm("ka", var("$va"))), var("$other")));
        test("[{$va=va, $other={kb=vb}}]", al(hm("ka", "va", "kb", "vb"), hm("kb", "vb")), al(new JammyMap(var("$other"), hm("ka", var("$va"))), var("$other")));

        test("[{$vb=va, $other={kb=va}}]", al(hm("ka", "va", "kb", "va"), hm("kb", "va")), al(new JammyMap(var("$other"), hm("ka", var("$vb"))), hm("kb", var("$vb"))));

        test("[]", al(hm("ka", "va", "kb", "va"), hm("kb", "vb")), al(new JammyMap(var("$other"), hm("ka", var("$vb"))), hm("kb", var("$vb"))));
    }

    @Test
    public void testMatchOr() {
        test("[{1=a, 2=b}]", al("a", "b", "b"), new JammyOr(al(al(var("x"), var("y"), var("x")), al(var("1"), var("2"), var("2")))));
        test("[]", al("a", "a", "b"), new JammyOr(al(al(var("x"), var("y"), var("x")), al(var("1"), var("2"), var("2")))));
        test("[{x=a, y=b}]", al("a", "b", "a"), new JammyOr(al(al(var("x"), var("y"), var("x")), al(var("1"), var("2"), var("2")))));
    }

}
