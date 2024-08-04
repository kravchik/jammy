package yk.jcommon.jammy;

import org.junit.Test;

import static yk.jammy.JammyShortNames.*;
import static yk.jcommon.jammy.JammyTestUtils.test;
import static yk.ycollections.YArrayList.al;

/**
 * 01.08.2024
 */
public class TestJammyByIndex {
    @Test
    public void testList() {
        test(null, al(), i(1, null));
        test(null, al("a", "b"), i(1, null));
        test("[{}]", al("a", null), i(1, null));
        test(null, al("a", null), i(-1, null));
        test("[{}]", al("a", null), i(null));
        test("[{}]", al("a", "b"), i(1, "b"));
        test("[{}]", al("a", "b"), i("b"));

        test(null, al(), i(var("i"), null));
        test(null, al("a", "b"), i(var("i"), null));
        test("[{i=1}]", al("a", null), i(var("i"), null));
        test("[{i=1}]", al("a", "b"), i(var("i"), "b"));

        test(null, al("a", al("b")), al(var("A"), i(var("A"), "b")));
    }
    
    @Test
    public void testArray() {
        //test(null, ar(), i(1, null));
        //test(null, ar("a", "b"), i(1, null));
        //test("[{}]", ar("a", null), i(1, null));
        //test(null, ar("a", null), i(-1, null));
        test("[{}]", ar("a", null), i(null));
        test("[{}]", ar("a", "b"), i(1, "b"));
        test("[{}]", ar("a", "b"), i("b"));

        test(null, ar(), i(var("i"), null));
        test(null, ar("a", "b"), i(var("i"), null));
        test("[{i=1}]", ar("a", null), i(var("i"), null));
        test("[{i=1}]", ar("a", "b"), i(var("i"), "b"));

        test(null, al("a", ar("b")), al(var("A"), i(var("A"), "b")));
    }

    @Test
    public void testList3() {
        test(null, al(), i3(any(), "b", any()));
        test(null, al(), i3(any(), any(), any()));//TODO or this matches?
        test("[{}]", al("a"), i3(any(), "a", any()));
        test("[{$a=[]}]", al("a"), i3(var("$a"), "a", any()));
        test("[{$a=[]}]", al("a"), i3(any(), "a", var("$a")));
        test("[{$a=[]}]", al("a"), i3(var("$a"), "a", var("$a")));
        test("[{}]", al("a", "b"), i3(any(), "b", any()));
        test("[{$a=[a]}]", al("a", "b"), i3(var("$a"), "b", any()));
        test(null, al("a", "b", "c"), i3(var("$a"), "a", var("$a")));
        test("[{$a=[], $b=[b, c]}]", al("a", "b", "c"), i3(var("$a"), "a", var("$b")));

        test("[{$a=[], $b=[a, c]}, {$a=[a], $b=[c]}]", al("a", "a", "c"), i3(var("$a"), "a", var("$b")));

        test("[{$a=[]}]", al("a"), i3(var("$a"), "a", any()));
        test(null, al("a"), i3(var("$a"), "a", any(), 1));

        test(null, al("a", "b"), i3(var("$a"), "b", any(), 0));
        test("[{$a=[a]}]", al("a", "b"), i3(var("$a"), "b", any(), 1));

        test("[{$i=1, $a=[a]}]", al("a", "b"), i3(var("$a"), "b", any(), var("$i")));
        test("[{$i=0, $a=[]}, {$i=1, $a=[a]}]", al("a", "b"), i3(var("$a"), any(), any(), var("$i")));
    }

    private static String[] ar(String... ss) {
        return ss;
    }
}
