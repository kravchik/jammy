package yk.jammy;

import yk.jammy.utils.BadException;
import yk.ycollections.YList;
import yk.ycollections.YMap;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static yk.ycollections.YArrayList.al;
import static yk.ycollections.YHashMap.hm;
import static yk.ycollections.YHashSet.hs;

/**
 * Created with IntelliJ IDEA.
 * User: yuri
 * Date: 28/10/15
 * Time: 16:54
 */
public class JammyMatcher {
    public YMap<Class, JammyCustomMatcher> byDataClass = hm();

    //TODO array
    //TODO set
    public YMap<Class, JammyCustomMatcher> byInstanceOf = hm(
        List.class, new JammyStandardList(),
        Map.class, new JammyStandardMap()
    );

    public YList<YMap<String, Object>> match(Object data, Object pattern) {
        return match(data, pattern, hm());
    }

    public YMap<String, Object> matchOne(Object data, Object pattern) {
        YList<YMap<String, Object>> result = match(data, pattern, hm());
        if (result == null) return null;
        if (result.size() > 1) throw BadException.shouldNeverReachHere();
        if (result.size() == 1) return result.car();
        return null;
    }

    public YList<YMap<String, Object>> match(Object data, Object pattern, YMap<String, Object> cur) {
        JammyCustomMatcher matcher = findMatcher(data, pattern);
        if (matcher != null) return matcher.match(this, data, pattern, cur);

        return Objects.equals(data, pattern) ? al(cur) : null;
    }

    public JammyCustomMatcher findMatcher(Object data, Object pattern) {
        if (pattern instanceof JammyCustomPattern) return ((JammyCustomPattern) pattern);
        if (data != null) {
            JammyCustomMatcher classMatcher = byDataClass.get(data.getClass());
            if (classMatcher != null) return classMatcher;

            if (pattern != null) {
                for (Map.Entry<Class, JammyCustomMatcher> entry : byInstanceOf.entrySet()) {
                    if (entry.getKey().isAssignableFrom(pattern.getClass())
                        && entry.getKey().isAssignableFrom(data.getClass())) return entry.getValue();
                }
            }
        }
        return null;
    }

    public static Object resolve(Object p, YMap<String, Object> cur) {
        if (p instanceof JammyVar && cur.containsKey(((JammyVar)p).name)) return cur.get(((JammyVar)p).name);
        return p;
    }

    public YList<YMap<String, Object>> match(Object data, Object pattern, YList<YMap<String, Object>> mm) {
        if (mm == null) return null;
        YList<YMap<String, Object>> result = null;
        for (YMap<String, Object> m : mm) {
            YList<YMap<String, Object>> res = match(data, pattern, m);
            if (res != null) {
                if (result == null) result = al();
                result.addAll(res);
            }
        }
        return result;
    }

    public YList<YMap<String, Object>> addMatch(YList<YMap<String, Object>> result,
                                                Object data, Object pattern,
                                                YMap<String, Object> cur) {
        return addInit(result, match(data, pattern, cur));
    }

    public static YList<YMap<String, Object>> addInit(YList<YMap<String, Object>> result, YList<YMap<String, Object>> newResult) {
        if (newResult == null) return result;
        if (result == null) result = al();
        result.addAll(newResult);
        return result;
    }
}
