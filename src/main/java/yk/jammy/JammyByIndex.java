package yk.jammy;

import yk.ycollections.YList;
import yk.ycollections.YMap;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static yk.jammy.JammyShortNames.any;
import static yk.ycollections.YArrayList.toYList;

/**
 * Created with IntelliJ IDEA.
 * User: yuri
 * Date: 28/10/15
 * Time: 16:43
 */
public class JammyByIndex implements JammyCustomPattern<JammyByIndex> {
    public Object index;
    public Object value;

    //TODO for array
    //TODO explicitly state should match array or not
    public Object before;
    public Object after;
    public Object rest;

    public JammyByIndex() {
    }

    public JammyByIndex(Object index, Object value) {
        this.index = index;
        this.value = value;
    }

    public JammyByIndex(Object index, Object value, Object before, Object after, Object rest) {
        this.index = index;
        this.value = value;
        this.before = before;
        this.after = after;
        this.rest = rest;
    }

    @Override
    public YList<YMap<String, Object>> match(JammyMatcher matcher, Object data, YMap<String, Object> cur) {
        if (data instanceof List) return matchList(matcher, (List) data, cur);
        if (data.getClass().isArray()) return matchArray(matcher, data, cur);
        return null;
    }

    private YList<YMap<String, Object>> matchArray(JammyMatcher matcher, Object data, YMap<String, Object> cur) {
        Object resolvedIndex = JammyMatcher.resolve(index, cur);
        int len = Array.getLength(data);
        if (resolvedIndex instanceof Number) {
            int i = ((Number) resolvedIndex).intValue();
            if (i < 0 || i >= len) return null;
            return matcher.match(Array.get(data, i), value, cur);
        }
        if (resolvedIndex != null && resolvedIndex != any() && !(resolvedIndex instanceof JammyVar)) return null;

        YList<YMap<String, Object>> result = null;
        for (int i = 0; i < len; i++) {
            result = matcher.addMatch(result, Array.get(data, i), value,
                resolvedIndex instanceof JammyVar ? cur.with(((JammyVar) resolvedIndex).name, i) : cur);
        }
        return result;
    }

    private YList<YMap<String, Object>> matchList(JammyMatcher matcher, List data, YMap<String, Object> cur) {
        Object resolvedIndex = JammyMatcher.resolve(index, cur);
        int len = data.size();
        if (index instanceof Number) {
            int i = ((Number) resolvedIndex).intValue();
            if (i < 0 || i >= len) return null;
            return forI(matcher, data, i, cur);
        }

        if (resolvedIndex != null && resolvedIndex != any() && !(resolvedIndex instanceof JammyVar)) return null;

        YList<YMap<String, Object>> result = null;
        for (int i = 0; i < len; i++) {

            result = JammyMatcher.addInit(result, forI(matcher, data, i,
                index instanceof JammyVar ? cur.with(((JammyVar) index).name, i) : cur));
        }
        return result;
    }

    private YList<YMap<String, Object>> forI(JammyMatcher matcher, List data, int i, YMap<String, Object> cur) {
        YList<YMap<String, Object>> result = matcher.match(data.get(i), value, cur);

        if (before != null && before != any()) result = matcher.match(toYList(data.subList(0, i)), before, result);
        if (after != null && after != any()) result = matcher.match(toYList(data.subList(i + 1, data.size())), after, result);
        if (rest != null && rest != any()) {
            YList dd = toYList(data);
            dd.remove(i);
            result = matcher.match(dd, rest, result);
        }
        return result;
    }

    private static YMap<String, Object> addInit(YMap<String, Object> init, YMap<String, Object> cur,
                                                String key, Object value) {
        if (cur == null) cur = init.with(key, value);
        cur.put(key, value);
        return cur;
    }

    @Override
    public String toString() {
        return "JammyByIndex{" +
            "index=" + index +
            ", value=" + value +
            ", before=" + before +
            ", after=" + after +
            ", rest=" + rest +
            '}';
    }
}
