package yk.jcommon.match2;

import yk.ycollections.YMap;
import yk.ycollections.YSet;

import static yk.ycollections.YHashSet.hs;

/**
 * Created with IntelliJ IDEA.
 * User: yuri
 * Date: 28/10/15
 * Time: 17:38
 */
public class MatchAny implements MatchCustomPattern {
    @Override
    public YSet<YMap<String, Object>> match(Matcher matcher, Object data, YMap<String, Object> cur) {
        return hs(cur);
    }
}
