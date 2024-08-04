package yk.jammy;

import yk.ycollections.YList;
import yk.ycollections.YMap;

import java.util.Map;

import static yk.ycollections.YArrayList.al;
import static yk.ycollections.YHashSet.hs;

/**
 * 13.03.2024
 * TODO rename
 */
public class JammyStandardMap implements JammyCustomMatcher<Map, Map> {

    @Override
    public YList<YMap<String, Object>> match(JammyMatcher matcher, Map data, Map pattern, YMap<String, Object> cur) {
        if (!data.keySet().containsAll(pattern.keySet())) return null;

        YList<YMap<String, Object>> last = al(cur);
        for (Object pk : pattern.keySet()) {
            Object pv = pattern.get(pk);
            Object dv = data.get(pk);

            YList<YMap<String, Object>> newResult = al();
            for (YMap<String, Object> map : last) matcher.addMatch(newResult, dv, pv, map);
            last = newResult;
        }
        return last;
    }

    //TODO match keys
    //  it could generate a lot, so maybe by some KeyMapsCustomMatch

}
