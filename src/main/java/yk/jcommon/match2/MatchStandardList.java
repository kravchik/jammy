package yk.jcommon.match2;

import yk.ycollections.YMap;
import yk.ycollections.YSet;

import java.util.List;

import static yk.ycollections.YHashSet.hs;

/**
 * 13.03.2024
 */
public class MatchStandardList implements MatchCustomMatcher {
    @Override
    public YSet<YMap<String, Object>> match(Matcher matcher, Object data, Object pattern, YMap<String, Object> cur) {
        return matchList(matcher, data, (List) pattern, cur);
    }

    private static YSet<YMap<String, Object>> matchList(Matcher matcher, Object data, List pattern, YMap<String, Object> cur) {
        if (!(data instanceof List)) return hs();
        List dl = (List) data;
        if (pattern.size() != dl.size()) return hs();

        YSet<YMap<String, Object>> last = hs(cur);

        for (int i = 0; i < pattern.size(); i++) {
            Object d = dl.get(i);
            Object p = pattern.get(i);
            if (last.isEmpty()) break;
            YSet<YMap<String, Object>> newResult = hs();
            for (YMap<String, Object> map : last) newResult.addAll(matcher.match(d, p, map));
            last = newResult;
        }

        return last;
    }


}
