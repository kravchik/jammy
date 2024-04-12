package yk.jcommon.match2;

import yk.ycollections.YMap;
import yk.ycollections.YSet;

import java.lang.reflect.Array;
import java.util.List;

import static yk.ycollections.YHashSet.hs;

/**
 * Created with IntelliJ IDEA.
 * User: yuri
 * Date: 28/10/15
 * Time: 16:43
 */
public class MatchByIndex implements MatchCustomPattern {
    public Object index;
    public Object value;

    public MatchByIndex(Object value) {
        index = new MatchAny();
        this.value = value;
    }

    public MatchByIndex() {
        index = new MatchAny();
    }

    public MatchByIndex(Object index, Object value) {
        this.index = index;
        this.value = value;
    }

    @Override
    public YSet<YMap<String, Object>> match(Matcher matcher, Object data, YMap<String, Object> cur) {
        return match(matcher, data, cur, this.value, this.index);
    }

    private static YSet<YMap<String, Object>> match(Matcher matcher, Object data, YMap<String, Object> cur, Object pattern, Object index) {
        if (data instanceof List) {
            List l = (List) data;
            Object resolvedIndex = Matcher.resolve(index, cur);
            if (index instanceof Number) return matcher.match(l.get(((Number) resolvedIndex).intValue()), pattern, cur);
            YSet<YMap<String, Object>> result = hs();
            for (int i = 0; i < l.size(); i++) {
                result.addAll(matcher.match(l.get(i), pattern,
                    resolvedIndex instanceof MatchVar ? cur.with(((MatchVar) resolvedIndex).name, i) : cur));
            }
            return result;
        }
        if (data.getClass().isArray()) {
            Object resolvedIndex = Matcher.resolve(index, cur);
            if (resolvedIndex instanceof Number) return matcher.match(Array.get(data, ((Number) resolvedIndex).intValue()), pattern, cur);
            YSet<YMap<String, Object>> result = hs();
            for (int i = 0, len = Array.getLength(data); i < len; i++) {
                result.addAll(matcher.match(Array.get(data, i), pattern,
                    resolvedIndex instanceof MatchVar ? cur.with(((MatchVar) resolvedIndex).name, i) : cur));
            }
            return result;
        }
        return hs();
    }
}
