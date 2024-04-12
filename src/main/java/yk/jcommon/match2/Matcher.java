package yk.jcommon.match2;

import yk.jcommon.utils.BadException;
import yk.ycollections.YMap;
import yk.ycollections.YSet;

import java.util.List;
import java.util.Map;

import static yk.ycollections.YHashMap.hm;
import static yk.ycollections.YHashSet.hs;

/**
 * Created with IntelliJ IDEA.
 * User: yuri
 * Date: 28/10/15
 * Time: 16:54
 *
 * TODO rename Jammy ?
 * TODO extract as a lib
 */
public class Matcher {
    public YMap<Class, MatchCustomMatcher> matchersByClass = hm();

    //TODO array
    //TODO set
    public YMap<Class, MatchCustomMatcher> matchersByInstanceof = hm(
        List.class, new MatchStandardList(),
        Map.class, new MatchStandardMap()
    );

    public YSet<YMap<String, Object>> match(Object data, Object pattern) {
        return match(data, pattern, hm());
    }

    public YMap<String, Object> matchOne(Object data, Object pattern) {
        YSet<YMap<String, Object>> result = match(data, pattern, hm());
        if (result.size() > 1) throw BadException.shouldNeverReachHere();
        if (result.size() == 1) return result.car();
        return null;
    }

    public YSet<YMap<String, Object>> match(Object data, Object pattern, YMap<String, Object> cur) {
        if (pattern instanceof MatchCustomPattern) return ((MatchCustomPattern) pattern).match(this, data, cur);

        if (data == null) return pattern == null ? hs(cur) : hs();

        MatchCustomMatcher classMatcher = matchersByClass.get(data.getClass());
        if (classMatcher != null) return classMatcher.match(this, data, pattern, cur);

        for (Map.Entry<Class, MatchCustomMatcher> entry : matchersByInstanceof.entrySet()) {
            if (entry.getKey().isAssignableFrom(pattern.getClass())) {
                return entry.getValue().match(this, data, pattern, cur);
            }
        }

        return data.equals(pattern) ? hs(cur) : hs();
    }

    public static Object resolve(Object p, YMap<String, Object> cur) {
        if (p instanceof MatchVar && cur.containsKey(((MatchVar)p).name)) return cur.get(((MatchVar)p).name);
        return p;
    }

}
