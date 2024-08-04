package yk.jammy;

import yk.ycollections.YList;
import yk.ycollections.YMap;

import java.util.List;

import static yk.ycollections.YArrayList.al;

/**
 * 13.03.2024
 */
public class JammyStandardList implements JammyCustomMatcher<List, List> {
    @Override
    public YList<YMap<String, Object>> match(JammyMatcher matcher, List data, List pattern, YMap<String, Object> cur) {
        if (pattern.size() != data.size()) return null;

        YList<YMap<String, Object>> last = al(cur);
        for (int i = 0; i < pattern.size(); i++) {
            last = matcher.match(data.get(i), pattern.get(i), last);
            if (last == null || last.isEmpty()) break;
        }
        return last;
    }


}
