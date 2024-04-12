package yk.jcommon.match2;

import yk.ycollections.YList;
import yk.ycollections.YMap;
import yk.ycollections.YSet;

import java.util.List;

import static yk.ycollections.YArrayList.toYList;
import static yk.ycollections.YHashSet.hs;

/**
 * Created with IntelliJ IDEA.
 * User: yuri
 * Date: 28/08/16
 * Time: 11:14
 */
public class MatchListFast implements MatchCustomPattern {//TODO rename?
    public YList pattern;

    public MatchListFast(YList pattern) {
        this.pattern = pattern;
    }

    @Override
    public YSet<YMap<String, Object>> match(Matcher matcher, Object data, YMap<String, Object> cur) {
        if (!(data instanceof List)) return hs();
        return matchRest(matcher, data instanceof YList ? (YList) data : toYList((List) data), this.pattern, cur);
    }

    public static YSet<YMap<String, Object>> matchRest(Matcher matcher, YList data, YList pattern, YMap<String, Object> cur) {

        for (int i = 0; i < pattern.size(); i++) {
            Object p = pattern.get(i);



        }

        return null;
    }

    public static YSet<YMap<String, Object>> mix(YSet<YMap<String, Object>> curr, YSet<YMap<String, Object>> newMap) {
        YSet<YMap<String, Object>> result = hs();
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
