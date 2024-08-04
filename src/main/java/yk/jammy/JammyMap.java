package yk.jammy;

import yk.ycollections.YList;
import yk.ycollections.YMap;

import java.util.Map;

import static yk.ycollections.YArrayList.al;
import static yk.ycollections.YHashMap.toYMap;
import static yk.ycollections.YHashSet.hs;

/**
 * Created with IntelliJ IDEA.
 * User: yuri
 * Date: 28/08/16
 * Time: 10:44
 */
//TODO match keys
public class JammyMap implements JammyCustomPattern<JammyMap> {
    public YMap pattern;
    public JammyVar other;

    public JammyMap(JammyVar other, YMap pattern) {
        this.other = other;
        this.pattern = pattern;
    }

    @Override
    public YList<YMap<String, Object>> match(JammyMatcher matcher, Object dObj, YMap<String, Object> cur) {
        if (!(dObj instanceof Map)) return null;
        Map data = (Map) dObj;
        if (!data.keySet().containsAll(this.pattern.keySet())) return null;

        YList<YMap<String, Object>> last = al(cur);
        for (Object pk : this.pattern.keySet()) {
            Object pv = this.pattern.get(pk);
            Object dv = data.get(pk);

            YList<YMap<String, Object>> newResult = al();
            for (YMap<String, Object> map : last) newResult.addAll(matcher.match(dv, pv, map));
            last = newResult;
        }
        YList<YMap<String, Object>> result = al();
        if (other != null) {
            YMap otherData = toYMap(data).without(this.pattern.keySet());
            for (YMap<String, Object> map : last) {
                result.addAll(matcher.match(otherData, other, map));
            }
        }
        return result;
    }
}
