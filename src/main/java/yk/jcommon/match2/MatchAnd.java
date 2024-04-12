package yk.jcommon.match2;

import yk.ycollections.YList;
import yk.ycollections.YMap;
import yk.ycollections.YSet;

import static yk.ycollections.YArrayList.al;
import static yk.ycollections.YHashSet.hs;

/**
 * Created with IntelliJ IDEA.
 * User: yuri
 * Date: 28/10/15
 * Time: 17:43
 */
public class MatchAnd implements MatchCustomPattern {
    public YList elements;

    public MatchAnd(Object... elements) {
        this.elements = al(elements);
    }

    @Override
    public YSet<YMap<String, Object>> match(Matcher matcher, Object data, YMap<String, Object> cur) {
        return matchAnd(matcher, data, elements, cur);
    }

    public static YSet<YMap<String, Object>> matchAnd(Matcher matcher, Object data, YList pattern,
                                                      YMap<String, Object> cur) {
        if (pattern.isEmpty()) return hs(cur);
        YList rest = pattern.cdr();
        return matcher.match(data, pattern.car(), cur)
            .flatMap(m -> matchAnd(matcher, data, rest, m));
    }


}
