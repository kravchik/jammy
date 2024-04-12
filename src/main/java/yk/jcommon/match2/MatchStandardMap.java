package yk.jcommon.match2;

import yk.ycollections.YMap;
import yk.ycollections.YSet;

import java.util.Map;

import static yk.ycollections.YHashSet.hs;

/**
 * 13.03.2024
 * TODO rename
 */
public class MatchStandardMap implements MatchCustomMatcher {

    @Override
    public YSet<YMap<String, Object>> match(Matcher matcher, Object data, Object pattern, YMap<String, Object> cur) {
        return matchMap(matcher, data, (Map) pattern, cur);
    }

    //TODO match keys
    //  it could generate a lot, so maybe by some KeyMapsCustomMatch
    private static YSet<YMap<String, Object>> matchMap(Matcher matcher, Object dObj, Map pattern, YMap<String, Object> cur) {
        if (!(dObj instanceof Map)) return hs();
        Map data = (Map) dObj;
        if (!data.keySet().containsAll(pattern.keySet())) return hs();

        YSet<YMap<String, Object>> last = hs(cur);
        for (Object pk : pattern.keySet()) {
            Object pv = pattern.get(pk);
            Object dv = data.get(pk);

            YSet<YMap<String, Object>> newResult = hs();
            for (YMap<String, Object> map : last) newResult.addAll(matcher.match(dv, pv, map));
            last = newResult;
        }
        return last;
    }

}
