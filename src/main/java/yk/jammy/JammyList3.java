package yk.jammy;

import yk.jammy.utils.BadException;
import yk.ycollections.YList;
import yk.ycollections.YMap;

import java.lang.reflect.Array;
import java.util.List;

import static yk.ycollections.YArrayList.al;

/**
 * Created with IntelliJ IDEA.
 * User: yuri
 * Date: 28/10/15
 * Time: 16:43
 */
@Deprecated //use JammyByIndex
public class JammyList3 implements JammyCustomPattern<JammyList3> {
    public Object before;
    public Object x;
    public Object after;
    public Object index;

    public JammyList3(Object before, Object x, Object after) {
        this.before = before;
        this.x = x;
        this.after = after;
    }

    public JammyList3(Object before, Object x, Object after, Object index) {
        this.before = before;
        this.x = x;
        this.after = after;
        this.index = index;
    }

    @Override
    public YList<YMap<String, Object>> match(JammyMatcher matcher, Object data, YMap<String, Object> cur) {
        if (data instanceof List) {
            List l = (List) data;
            Object index = matcher.resolve(this.index, cur);

            if (this.index instanceof Number) {
                return forI(matcher, cur, l, ((Number) index).intValue());
            }
            YList<YMap<String, Object>> result = al();
            for (int i = 0; i < l.size(); i++) result.addAll(forI(matcher, cur, l, i));
            return result;
        }
        if (data.getClass().isArray()) {
            Object index = matcher.resolve(this.index, cur);
            if (index instanceof Number) return matcher.match(Array.get(data, ((Number) index).intValue()), this.x, cur);
            YList<YMap<String, Object>> result = al();
            if (!(index instanceof JammyVar) && !(index instanceof JammyAny)) BadException.shouldNeverReachHere("" + index);

            for (int i = 0; i < Array.getLength(data); i++) {
                for (YMap<String, Object> m : matcher.match(Array.get(data, i), this.x, index instanceof JammyVar ? cur.with(((JammyVar) index).name, i) : cur)) {
//                    if (pattern.index == null) result.add(m);
//                    else result.addAll(match(i, index, m));
                    result.add(m);
                }
            }
            return result;
        }
        return null;
    }

    private YList<YMap<String, Object>> forI(JammyMatcher matcher, YMap<String, Object> cur, List l, int i) {
        if (i < 0 || i >= l.size()) return null;
        YList<YMap<String, Object>> res = matcher.match(i <= 0 ? al() : l.subList(0, i), before, cur);

        YList<YMap<String, Object>> res2 = al();
        for (YMap<String, Object> m : res) matcher.addMatch(res2, l.get(i), this.x, m);

        YList<YMap<String, Object>> res3 = al();
        for (YMap<String, Object> m : res2) matcher.addMatch(res3, i >= l.size() - 1 ? al() : l.subList(i + 1, l.size()), after, m);

        YList<YMap<String, Object>> result = al();

        for (YMap<String, Object> m : res3) {
            if (index == null) result.add(m);
            //TODO don't match if we know index already?
            else matcher.addMatch(result, i, index, m);
        }

        return result;
    }
}
