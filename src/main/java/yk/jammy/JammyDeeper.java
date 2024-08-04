package yk.jammy;

import yk.ycollections.YList;
import yk.ycollections.YMap;

import static yk.ycollections.YArrayList.al;
import static yk.ycollections.YHashSet.hs;

/**
 * Created with IntelliJ IDEA.
 * User: yuri
 * Date: 31/10/15
 * Time: 11:46
 */
public class JammyDeeper implements JammyCustomPattern<JammyDeeper> {
    public YList<Object> accessorPatterns;
    public Object rest;

    //TODO assert one var with name "access" (?)
    public JammyDeeper(YList<Object> accessorPatterns, Object rest) {
        this.accessorPatterns = accessorPatterns;
        this.rest = rest;
    }

    public JammyDeeper(YList<Object> accessorPatterns) {
        this.accessorPatterns = accessorPatterns;
    }

    @Override
    public YList<YMap<String, Object>> match(JammyMatcher matcher, Object data, YMap<String, Object> cur) {
        YList<YMap<String, Object>> result = al();
        result.addAll(matcher.match(data, rest, cur));
        for (Object accessorPattern : accessorPatterns) {
            YList<YMap<String, Object>> variants = matcher.match(data, accessorPattern);
            for (YMap<String, Object> variant : variants) {
                result.addAll(matcher.match(variant.get("access"), rest, cur));
                result.addAll(matcher.match(variant.get("access"), this, cur));
            }
        }
        return result;
    }

    private static boolean tryDeeper(Object data) {
        if (data == null) return false;
        if (data.getClass().isPrimitive()) return false;
        if (data.getClass().getName().startsWith("java.lang.")) return false;
        if (data.getClass().isEnum()) return false;
        if (data instanceof String) return false;
        if (data instanceof Boolean) return false;
        return true;
    }

}
