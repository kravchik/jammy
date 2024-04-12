package yk.jcommon.match2;

import yk.ycollections.YMap;
import yk.ycollections.YSet;

import static yk.ycollections.YHashSet.hs;

/**
 * Created with IntelliJ IDEA.
 * User: yuri
 * Date: 28/10/15
 * Time: 16:47
 */
public class MatchVar implements MatchCustomPattern {
    public String name;
    public Object rest = new MatchAny();//TODO rename -> value

    public MatchVar(String name) {
        this.name = name;
    }

    public MatchVar(String name, Object rest) {
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
    public YSet<YMap<String, Object>> match(Matcher matcher, Object data, YMap<String, Object> cur) {
        if (cur.containsKey(name)) {
            if (!cur.get(name).equals(data)) return hs();
            return matcher.match(data, rest, cur);
        }
        YMap<String, Object> m = cur.with(name, data);
        return rest == null ? hs(m) : matcher.match(data, rest, m);
    }
}
