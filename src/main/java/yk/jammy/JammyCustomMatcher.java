package yk.jammy;

import yk.ycollections.YList;
import yk.ycollections.YMap;

/**
 * Created with IntelliJ IDEA.
 * User: yuri
 * Date: 19/08/16
 * Time: 11:02
 */
public interface JammyCustomMatcher<D, P> {
    YList<YMap<String, Object>> match(JammyMatcher matcher, D data, P pattern, YMap<String, Object> cur);
}
