package yk.jcommon.match3;

import yk.ycollections.YList;
import yk.ycollections.YMap;

import java.util.List;

/**
 * Created by Yuri Kravchik on 19/11/16.
 */
public class GenMatchList implements GenMatchCustom {
    public YList pattern;

    @Override
    public YMap<String, Object> next(GenMatcher matcher, Object data, YMap<String, Object> current) {
        if (!(data instanceof List)) return null;
        List dl = (List) data;


        return null;
    }
}
