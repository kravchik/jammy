package yk.jcommon.match2;

import yk.ycollections.YMap;
import yk.ycollections.YSet;

/**
 * Created with IntelliJ IDEA.
 * User: yuri
 * Date: 19/08/16
 * Time: 11:02
 */
public interface MatchCustomMatcher {
    YSet<YMap<String, Object>> match(Matcher matcher, Object data, Object pattern, YMap<String, Object> cur);
}
