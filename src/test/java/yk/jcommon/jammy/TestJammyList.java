package yk.jcommon.jammy;

import org.junit.Ignore;
import org.junit.Test;
import yk.jammy.JammyList;
import yk.jammy.JammyMatcher;
import yk.ycollections.YArrayList;
import yk.ycollections.YList;
import yk.ycollections.YMap;
import yk.ycollections.YSet;

import static org.junit.Assert.assertEquals;
import static yk.jammy.JammyShortNames.*;
import static yk.jammy.JammyShortNames.ml;
import static yk.jcommon.jammy.JammyTestUtils.test;
import static yk.ycollections.YArrayList.al;
import static yk.ycollections.YHashMap.hm;
import static yk.ycollections.YHashSet.hs;

/**
 * Created by Yuri Kravchik on 18/11/16.
 */
public class TestJammyList {

    @Test
    public void testList() {
        test("[{before=a}]", al("a", "b", "c"),
            new JammyList(al(var("before"), "b", "c")));
        test("[{before=a, after=c}]", al("a", "b", "c"),
            new JammyList(al(var("before"), "b", var("after"))));

        test("[{before=[a], between=[]}]", al("a", "b", "c"),
            new JammyList(al(listFiller("before"), "b", listFiller("between"), "c")));
        test("[{before=[a], between=[], after=[]}]", al("a", "b", "c"),
            new JammyList(al(listFiller("before"), "b", listFiller("between"), "c", listFiller("after"))));

        assertEquals("[{v=name0, before=[], between=[[first, name1]], after=[[second, name1]]}, " +
                      "{v=name1, before=[[first, name0]], between=[[second, name0]], after=[]}]",
                new JammyMatcher().match(genCommands(2), new JammyList(al(
                        listFiller("before"),
                        al("first", var("v")),
                        listFiller("between"),
                        al("second", var("v")),
                        listFiller("after"))
                )).toString());
    }

    public static void main(String[] args) {
        testPerformance(50);
        testPerformance(100);
        testPerformance(200);
        testPerformance(500);
        testPerformance(1000);
        testPerformance(2000);
        testPerformance(10000);

    }
    
    private static void testPerformance(int count) {
        YList l = genCommands(count);

        //StopWatch sw = new StopWatch();
        YList<YMap<String, Object>> result = new JammyMatcher().match(l, new JammyList(al(listFiller("before"), al("first", var("var")), listFiller("between"), al("second", var("var")), listFiller("after"))));
        System.out.println(result.size());
        //sw.stop();
        //System.out.println(count + " took: " + sw.toString());
    }

    private static YList genCommands(int count) {
        YList l = al();
        for (int i = 0; i < count; i++) l.add(al("first", "name" + i));
        for (int i = 0; i < count; i++) l.add(al("second", "name" + i));
        return l;
    }

    private static void testPerformance2(int count) {
        YList l = genCommands(count);

        //StopWatch sw = new StopWatch();

        YArrayList<Object> first = al("first", var("var"));
        YArrayList<Object> second = al("second", var("var"));
        JammyMatcher matcher = new JammyMatcher();
        YSet<YMap> result1 = hs();
        YSet<YMap> result2 = hs();

        YMap<String, Boolean> res = hm();
        for (int i = 0; i < l.size(); i++) {
            for (YMap m : result1) result2.addAll(matcher.match(l.get(i), second, m));
            result1.addAll(matcher.match(l.get(i), first));
        }

        //System.out.println(result2.size());
        //sw.stop();
        //System.out.println(count + " took: " + sw.toString());
    }

    private static void testPerformance2b(int count) {
        YList l = genCommands(count);

        //StopWatch sw = new StopWatch();

        YArrayList<Object> first = al("first", var("var"));
        YArrayList<Object> second = al("second", var("var"));
        JammyMatcher matcher = new JammyMatcher();
        YSet<YMap> result1 = hs();
        YSet<YMap> result2 = hs();

        YMap<String, Boolean> res = hm();
        for (int i = 0; i < l.size(); i++) {
            YMap<String, Object> s = matcher.matchOne(l.get(i), second);
            if (s != null) {
                a:
                for (YMap r1 : result1) {
                    for (String k : s.keySet()) {
                        if (r1.containsKey(k) && !r1.get(k).equals(s.get(k))) {
                            continue a;
                        }
                    }
                    result2.add(r1.with(s));
                }
            }
            result1.addAll(matcher.match(l.get(i), first));
        }

        //System.out.println(result2.size());
        //sw.stop();
        //System.out.println(count + " took: " + sw.toString());
    }

    private static void testPerformance3(int count) {
        YList l = genCommands(count);

        //StopWatch sw = new StopWatch();

        YArrayList<Object> first = al("first", var("var"));
        YArrayList<Object> second = al("second", var("var"));
        JammyMatcher matcher = new JammyMatcher();
        YMap<String, Boolean> res = hm();
        for (int i = 0; i < l.size(); i++) {

            YMap<String, Object> f = matcher.matchOne(l.get(i), first);
            if (f != null) {
                String varName = (String) f.get("var");
                res.put(varName, false);
            }

            YMap<String, Object> s = matcher.matchOne(l.get(i), second);
            if (s != null) {
                String varName = (String) s.get("var");
                if (res.containsKey(varName)) {
                    res.put(varName, true);
                }
            }
        }

        //System.out.println(res.size());
        //sw.stop();
        //System.out.println(count + " took: " + sw.toString());
    }

    //TODO fix
    //@Test
    public void testMatchList() {
        test("[{}]", al(), ml());
        test("[{}]", al("a"), ml("a"));
        test("null", al("a"), ml());
        test("null", al(), ml("a"));
        test("null", "", ml("a"));

        test("[{$b=b}]", al("a", "b"), ml("a", var("$b")));
        test("null", al("a", "b"), ml(var("$b"), var("$b")));
        test("[{$a=a, $b=b}]", al("a", "b"), ml(var("$a"), var("$b")));

    }

    @Ignore //TODO fix
    @Test
    public void testMatchListFillers() {

        test("[{}]", al(), ml(listFiller()));
        test("[{}]", al("a"), ml(listFiller()));
        test("[{}]", al("a", "b"), ml(listFiller()));

        test("[{}]", al("a", "b"), ml(listFiller().setMaxLength(2)));
        test("[{}]", al(), ml(listFiller().setMaxLength(0)));
        test("null", al("a", "b"), ml(listFiller().setMaxLength(0)));
        test("null", al("a", "b"), ml(listFiller().setMaxLength(1)));

        test("[{}]", al("a", "b"), ml(listFiller().setMinLength(0)));
        test("[{}]", al("a", "b"), ml(listFiller().setMinLength(2)));
        test("[{}]", al(), ml(listFiller().setMinLength(0)));
        test("null", al("a", "b"), ml(listFiller().setMinLength(3)));

        test("null", al(), ml(listFiller().setMinLength(1).setMaxLength(2)));
        test("[{}]", al("a"), ml(listFiller().setMinLength(1).setMaxLength(2)));
        test("[{}]", al("a", "b"), ml(listFiller().setMinLength(1).setMaxLength(2)));
        test("null", al("a", "b", "c"), ml(listFiller().setMinLength(1).setMaxLength(2)));


        test("[{$v=a}, {$v=b}, {$v=c}]", al("a", "b", "c"), ml(listFiller(), var("$v"), listFiller()));
        test("[{$before=[], $v=a, $after=[b, c]}, {$before=[a], $v=b, $after=[c]}, {$before=[a, b], $v=c, $after=[]}]", al("a", "b", "c"), ml(listFiller("$before"), var("$v"), listFiller("$after")));
        test("[{$before1=[], $before2=[], $v=a, $after=[b, c]}, {$before1=[], $before2=[a], $v=b, $after=[c]}, {$before1=[], $before2=[a, b], $v=c, $after=[]}, {$before1=[a], $before2=[], $v=b, $after=[c]}, {$before1=[a], $before2=[b], $v=c, $after=[]}, {$before1=[a, b], $before2=[], $v=c, $after=[]}]", al("a", "b", "c"), ml(listFiller("$before1"), listFiller("$before2"), var("$v"), listFiller("$after")));
        test("[{$before=[], $v=a, $after1=[], $after2=[b, c]}, {$before=[], $v=a, $after1=[b], $after2=[c]}, {$before=[], $v=a, $after1=[b, c], $after2=[]}, {$before=[a], $v=b, $after1=[], $after2=[c]}, {$before=[a], $v=b, $after1=[c], $after2=[]}, {$before=[a, b], $v=c, $after1=[], $after2=[]}]", al("a", "b", "c"), ml(listFiller("$before"), var("$v"), listFiller("$after1"), listFiller("$after2")));
        test("[{$v1=a, $mid=[b], $v2=c}]", al("a", "b", "c"), ml(var("$v1"),listFiller("$mid"), var("$v2")));
        test("[{$v1=a, $mid=[], $v2=b, $after=[c]}, {$v1=a, $mid=[b], $v2=c, $after=[]}]", al("a", "b", "c"), ml(var("$v1"),listFiller("$mid"), var("$v2"), listFiller("$after")));

        test("[{$v1=a, $mid=[], $v2=b, $after=[c]}]", al(al("a", "b", "c"), al("c")), al(ml(var("$v1"),listFiller("$mid"), var("$v2"), listFiller("$after")), var("$after")));
        test("[{$after=[c], $v1=a, $mid=[], $v2=b}]", al(al("c"), al("a", "b", "c")), al(var("$after"), ml(var("$v1"),listFiller("$mid"), var("$v2"), listFiller("$after"))));

        test("[{$common=[]}, {$common=[b]}, {$common=[b, c]}, {$common=[c]}]", al(al("a", "b", "c"), al("b", "c", "d")), al(ml(listFiller(), listFiller("$common"), listFiller()), ml(listFiller(), listFiller("$common"), listFiller())));

    }


}