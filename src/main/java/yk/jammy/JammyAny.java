package yk.jammy;

import yk.ycollections.YList;
import yk.ycollections.YMap;

import static yk.ycollections.YArrayList.al;

/**
 * Created with IntelliJ IDEA.
 * User: yuri
 * Date: 28/10/15
 * Time: 17:38
 */
public class JammyAny implements JammyCustomPattern<JammyAny> {
    @Override
    public YList<YMap<String, Object>> match(JammyMatcher matcher, Object data, YMap<String, Object> cur) {
        return al(cur);
    }

    @Override
    public String toString() {
        return "any";
    }
}
