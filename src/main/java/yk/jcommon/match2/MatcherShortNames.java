package yk.jcommon.match2;

import yk.jcommon.utils.BadException;
import yk.jcommon.utils.Reflector;
import yk.ycollections.Tuple;
import yk.ycollections.YList;

import java.util.List;

import static yk.ycollections.YArrayList.al;

/**
 * Created with IntelliJ IDEA.
 * User: yuri
 * Date: 01/11/15
 * Time: 17:04
 */
public class MatcherShortNames {
    public static final Object OTHER = new Object();

    public static MatchObject obj(String name, Object... rest) {
        return MatchObject.obj(name, rest);
    }

    public static MatchObject objNamed(String name, boolean isMethod, Object... rest) {
        return MatchObject.objNamed(name, isMethod, rest);
    }

    public static MatchVar var(String name) {
        return new MatchVar(name);
    }

    public static MatchVar var(String name, Object rest) {
        return new MatchVar(name, rest);
    }

    public static MatchObject obj(Class c, Object... rest) {
        return MatchObject.obj("getClass", al(c).withAll((List) al(rest)).toArray());
    }

    public static MatchObject objNamed(Class c, Object... rest) {
        return MatchObject.objNamed("getClass", true, al(c).withAll((List) al(rest)).toArray());
    }

    public static MatchByIndex i(Object value) {
        return new MatchByIndex(value);
    }

    public static MatchByIndex i() {
        return new MatchByIndex();
    }

    public static MatchByIndex i(Object index, Object value) {
        return new MatchByIndex(index, value);
    }

    public static MatchList ml(Object... oo) {
        return new MatchList(al(oo));
    }

    public static MatchList.Filler listFiller() {
        return new MatchList.Filler();
    }

    public static MatchList.Filler listFiller(String varName) {
        return new MatchList.Filler().setInside(var(varName));
    }

    public static MatchList.Filler listFiller(Object inside) {
        return new MatchList.Filler().setInside(inside);
    }

    public static MatchDeeper deeper(YList<Object> accessorPatterns) {
        return new MatchDeeper(accessorPatterns);
    }

    public static Object stairs(Object... oo) {
        Object last = null;
        for (Object o : al(oo).reverse()) {
            if (last == null);
            else if (o instanceof MatchObject) {
                MatchObject p = (MatchObject) o;
                Tuple<String, MatchObject.PropertyDesc> lastPare = p.pp.last();
                if (lastPare.b.value != null) BadException.die("last pare in Property must be key to null, but was " + lastPare);
                lastPare.b.value = last;
            } else if (o instanceof MatchByIndex) {
                MatchByIndex bi = (MatchByIndex) o;
                if (bi.value != null) bi.index = bi.value;
                bi.value = last;
            } else {
                if (Reflector.getField(o.getClass(), "rest") == null) BadException.die("expected object with field 'rest', but was " + o);
                Object valueAtRest = Reflector.get(o, "rest");
                if (valueAtRest == null || valueAtRest.getClass() == MatchAny.class) {
                    Reflector.set(o, "rest", last);
                } else BadException.die("expected null at 'rest' but was " + valueAtRest);
            }
            last = o;
        }
        return last;
    }

}
