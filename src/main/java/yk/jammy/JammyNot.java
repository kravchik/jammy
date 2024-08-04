package yk.jammy;

import yk.ycollections.YList;
import yk.ycollections.YMap;

import static yk.ycollections.YArrayList.al;
import static yk.ycollections.YHashSet.hs;

/**
 * Created with IntelliJ IDEA.
 * User: yuri
 * Date: 28/10/15
 * Time: 16:49
 */
public class JammyNot implements JammyCustomPattern<JammyNot> {
    public Object rest;

    public JammyNot() {
    }

    public JammyNot(Object rest) {
        this.rest = rest;
    }

    @Override
    public YList<YMap<String, Object>> match(JammyMatcher matcher, Object data, YMap<String, Object> cur) {
        YList<YMap<String, Object>> mm = matcher.match(data, this.rest, cur);
        return mm == null || mm.isEmpty() ? al(cur) : null;
    }
}
