package yk.jcommon.match3;

import yk.ycollections.YMap;

/**
 * Created by Yuri Kravchik on 19/11/16.
 */
public interface GenMatchCustom {
    YMap<String, Object> next(GenMatcher matcher, Object data, YMap<String, Object> current);
}
