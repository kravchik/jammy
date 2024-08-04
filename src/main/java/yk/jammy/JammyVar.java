package yk.jammy;

import yk.ycollections.YList;
import yk.ycollections.YMap;

import java.util.Objects;

import static yk.ycollections.YArrayList.al;
import static yk.ycollections.YHashSet.hs;

/**
 * Created with IntelliJ IDEA.
 * User: yuri
 * Date: 28/10/15
 * Time: 16:47
 */
public class JammyVar implements JammyCustomPattern<JammyVar> {
    public String name;
    public Object rest = new JammyAny();//TODO rename -> value

    public JammyVar(String name) {
        this.name = name;
    }

    public JammyVar(String name, Object rest) {
        this.name = name;
        this.rest = rest;
    }

    @Override
    public String toString() {
        return "Var{" +
               "name='" + name + '\'' +
               ", rest=" + rest +
               '}';
    }

    @Override
    public YList<YMap<String, Object>> match(JammyMatcher matcher, Object data, YMap<String, Object> cur) {
        if (cur.containsKey(name)) {
            if (!Objects.equals(cur.get(name), data)) return null;
            return matcher.match(data, rest, cur);
        }
        YMap<String, Object> m = cur.with(name, data);
        return rest == null ? al(m) : matcher.match(data, rest, m);
    }
}
