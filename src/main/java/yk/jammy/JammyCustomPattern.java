package yk.jammy;

import yk.ycollections.YList;
import yk.ycollections.YMap;

/**
 * Created with IntelliJ IDEA.
 * User: yuri
 * Date: 19/08/16
 * Time: 11:02
 */
public interface JammyCustomPattern<T> extends JammyCustomMatcher<Object, T> {
    @Override
    default YList<YMap<String, Object>> match(JammyMatcher matcher, Object data, T pattern, YMap<String, Object> cur) {
        if (pattern != this) throw new RuntimeException("Should not be called with different pattern");
        return match(matcher, data, cur);
    }

    YList<YMap<String, Object>> match(JammyMatcher matcher, Object data, YMap<String, Object> cur);

}
