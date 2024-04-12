package yk.jcommon.match2;

import yk.ycollections.YMap;
import yk.ycollections.YSet;

import static yk.ycollections.YHashSet.hs;

/**
 * Created with IntelliJ IDEA.
 * User: yuri
 * Date: 28/10/15
 * Time: 16:49
 */
public class MatchNot implements MatchCustomPattern {
    public Object rest;

    public MatchNot() {
    }

    public MatchNot(Object rest) {
        this.rest = rest;
    }

    @Override
    public YSet<YMap<String, Object>> match(Matcher matcher, Object data, YMap<String, Object> cur) {
        return matcher.match(data, this.rest, cur).notEmpty() ? hs() : hs(cur);
    }
}
