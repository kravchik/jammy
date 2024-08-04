package yk.jammy;

import yk.ycollections.YList;
import yk.ycollections.YMap;

import static yk.ycollections.YArrayList.al;

/**
 * Created with IntelliJ IDEA.
 * User: yuri
 * Date: 28/10/15
 * Time: 17:43
 */
public class JammyAnd implements JammyCustomPattern<JammyAnd> {
    public YList elements;

    public JammyAnd(Object... elements) {
        this.elements = al(elements);
    }

    @Override
    public YList<YMap<String, Object>> match(JammyMatcher matcher, Object data, YMap<String, Object> cur) {
        return matchAnd(matcher, data, elements, cur);
    }

    public static YList<YMap<String, Object>> matchAnd(JammyMatcher matcher, Object data, YList pattern,
                                                      YMap<String, Object> cur) {
        if (pattern.isEmpty()) return al(cur);
        YList rest = pattern.cdr();
        return matcher.match(data, pattern.car(), cur)
            .flatMap(m -> matchAnd(matcher, data, rest, m));
    }


}
