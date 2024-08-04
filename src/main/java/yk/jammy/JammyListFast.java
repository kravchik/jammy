package yk.jammy;

import yk.ycollections.YList;
import yk.ycollections.YMap;

import java.util.List;

import static yk.ycollections.YArrayList.al;
import static yk.ycollections.YArrayList.toYList;
import static yk.ycollections.YHashSet.hs;

/**
 * Created with IntelliJ IDEA.
 * User: yuri
 * Date: 28/08/16
 * Time: 11:14
 */
public class JammyListFast implements JammyCustomPattern<JammyListFast> {//TODO rename?
    public YList pattern;

    public JammyListFast(YList pattern) {
        this.pattern = pattern;
    }

    @Override
    public YList<YMap<String, Object>> match(JammyMatcher matcher, Object data, YMap<String, Object> cur) {
        if (!(data instanceof List)) return null;
        return matchRest(matcher, data instanceof YList ? (YList) data : toYList((List) data), this.pattern, cur);
    }

    public static YList<YMap<String, Object>> matchRest(JammyMatcher matcher, YList data, YList pattern, YMap<String, Object> cur) {

        for (int i = 0; i < pattern.size(); i++) {
            Object p = pattern.get(i);



        }

        return null;
    }

    public static YList<YMap<String, Object>> mix(YList<YMap<String, Object>> curr, YList<YMap<String, Object>> newMap) {
        YList<YMap<String, Object>> result = al();
        a:for (YMap<String, Object> nm : newMap) {
            for (YMap<String, Object> cur : curr) {
                for (String k : nm.keySet()) {
                    if (nm.containsKey(k) && !cur.get(k).equals(nm.get(k))) {
                        continue a;
                    }
                }
                result.add(cur.with(nm));
            }
        }
        return result;
    }

    public static YMap<String, Object> mix(YMap<String, Object> cur, YMap<String, Object> nm) {
        for (String k : nm.keySet()) {
            if (nm.containsKey(k) && !cur.get(k).equals(nm.get(k))) {
                return null;
            }
        }
        return cur.with(nm);
    }


}
