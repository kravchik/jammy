package yk.jammy;

import yk.jammy.utils.BadException;
import yk.jammy.utils.Reflector;
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
public class JammyShortNames {
    public static final Object OTHER = new Object();
    public static final JammyAny ANY = new JammyAny();

    public static JammyObject obj(String name, Object... rest) {
        return JammyObject.obj(name, rest);
    }

    public static JammyObject objNamed(String name, boolean isMethod, Object... rest) {
        return JammyObject.objNamed(name, isMethod, rest);
    }

    public static JammyVar var(String name) {
        return new JammyVar(name);
    }

    public static JammyVar var(String name, Object rest) {
        return new JammyVar(name, rest);
    }

    public static JammyObject obj(Class c, Object... rest) {
        return JammyObject.obj("getClass", al(c).withAll((List) al(rest)).toArray());
    }

    public static JammyObject objNamed(Class c, Object... rest) {
        return JammyObject.objNamed("getClass", true, al(c).withAll((List) al(rest)).toArray());
    }

    public static JammyByIndex i(Object value) {
        return new JammyByIndex(null, value);
    }

    public static JammyByIndex i(Object index, Object value) {
        return new JammyByIndex(index, value);
    }

    public static Object i2(Object x, Object rest) {
        return new JammyByIndex(null, x, null, null, rest);
    }

    public static Object i2(Object index, Object x, Object rest) {
        return new JammyByIndex(index, x, null, null, rest);
    }

    public static Object i3(Object before, Object x, Object after, Object index) {
        return new JammyByIndex(index, x, before, after, null);
    }

    public static Object i3(Object before, Object x, Object after) {
        return new JammyByIndex(null, x, before, after, null);
    }

    public static JammyList ml(Object... oo) {
        return new JammyList(al(oo));
    }

    public static JammyList.Filler listFiller() {
        return new JammyList.Filler();
    }

    public static JammyList.Filler listFiller(String varName) {
        return new JammyList.Filler().setInside(var(varName));
    }

    public static JammyList.Filler listFiller(Object inside) {
        return new JammyList.Filler().setInside(inside);
    }

    public static JammyDeeper deeper(YList<Object> accessorPatterns) {
        return new JammyDeeper(accessorPatterns);
    }

    public static JammyAny any() {
        return ANY;
    }

    public static Object stairs(Object... oo) {
        Object last = null;
        for (Object o : al(oo).reversed()) {
            if (last == null);
            else if (o instanceof JammyObject) {
                JammyObject p = (JammyObject) o;
                Tuple<String, JammyObject.PropertyDesc> lastPare = p.pp.last();
                if (lastPare.b.value != null) BadException.die("last pare in Property must be key to null, but was " + lastPare);
                lastPare.b.value = last;
            } else if (o instanceof JammyByIndex) {
                JammyByIndex bi = (JammyByIndex) o;
                if (bi.value != null) bi.index = bi.value;
                bi.value = last;
            } else {
                if (Reflector.getField(o.getClass(), "rest") == null) BadException.die("expected object with field 'rest', but was " + o);
                Object valueAtRest = Reflector.get(o, "rest");
                if (valueAtRest == null || valueAtRest.getClass() == JammyAny.class) {
                    Reflector.set(o, "rest", last);
                } else BadException.die("expected null at 'rest' but was " + valueAtRest);
            }
            last = o;
        }
        return last;
    }

}
