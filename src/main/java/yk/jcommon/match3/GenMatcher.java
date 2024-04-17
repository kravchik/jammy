package yk.jcommon.match3;

import yk.jcommon.match2.MatchAny;
import yk.jcommon.match2.MatchVar;
import yk.jcommon.utils.Util;
import yk.ycollections.YMap;

import static yk.ycollections.YHashMap.hm;

/**
 * Created by Yuri Kravchik on 19/11/16.
 *
 * Generative matcher, which produces results as it goes, without need to collect all variations at once
 */
public class GenMatcher {
    private Object data;
    private Object pattern;

    public GenMatcher(Object data, Object pattern) {
        this.data = data;
        this.pattern = pattern;
    }

    public YMap<String, Object> next() {
        return next(data, pattern, hm());
    }

    public YMap<String, Object> next(Object data, Object pattern, YMap<String, Object> cur) {
        if (data == null) {
            return pattern == null ? cur : null;
            //TODO variable matches null
        }

        if (pattern instanceof MatchVar) return next(data, (MatchVar) pattern, cur);

        if (pattern instanceof GenMatchCustom) return ((GenMatchCustom) pattern).next(this, data, cur);
        if (pattern instanceof MatchAny) return cur;


        if (Util.equalsWithNull(data, pattern)) return cur;
        return null;
    }

    private YMap<String, Object> next(Object data, MatchVar pattern, YMap<String, Object> cur) {
        if (cur.containsKey(pattern.name)) {
            return cur.get(pattern.name).equals(data) ? cur : null;
        }
        YMap<String, Object> resMap = cur.with(pattern.name, data);
        if (pattern.rest != null) return next(data, pattern.rest, resMap);
        else return resMap;
    }
}
