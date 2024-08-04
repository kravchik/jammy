package yk.jammy;

import yk.jammy.utils.Reflector;
import yk.ycollections.Tuple;
import yk.ycollections.YList;
import yk.ycollections.YMap;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static yk.ycollections.YArrayList.al;
import static yk.ycollections.YHashMap.hm;

/**
 * Created with IntelliJ IDEA.
 * User: yuri
 * Date: 28/10/15
 * Time: 17:06
 */
public class JammyObject implements JammyCustomPattern<JammyObject> {
    public YMap<String, PropertyDesc> pp;

    public static JammyObject objNamed(String name, Boolean isMethod, Object... oo) {
        JammyObject result= new JammyObject();
        result.pp = hm();
        result.pp.put(name, oo.length > 0 ? new PropertyDesc(name, isMethod, oo[0]) : null);
        for (int i = 1; i < oo.length; i += 3) {
            String k = (String) oo[i];
            Boolean isM = (Boolean) oo[i + 1];
            Object v = i+2 == oo.length ? null : oo[i+2];
            result.pp.put(k, new PropertyDesc(k, isM, v));
        }
        return result;
    }

    public static JammyObject obj(String name, Object... oo) {
        JammyObject result= new JammyObject();
        result.pp = hm();
        result.pp.put(name, new PropertyDesc(name, null, oo.length > 0 ? oo[0] : null));
        for (int i = 1; i < oo.length; i += 2) {
            String k = (String) oo[i];
            Boolean isM = null;
            Object v = i+1 == oo.length ? null : oo[i+1];
            result.pp.put(k, new PropertyDesc(k, isM, v));
        }
        return result;
    }

    @Override
    public YList<YMap<String, Object>> match(JammyMatcher matcher, Object data, YMap<String, Object> cur) {
        if (data == null) return null;
        return matchProps(matcher, data, pp, cur);
    }

    private static YList<YMap<String, Object>> matchProps(JammyMatcher matcher, Object data,
                                                         YMap<String, JammyObject.PropertyDesc> pp, YMap<String, Object> cur) {
        if (pp.isEmpty()) return al(cur);
        JammyObject.PropertyDesc car = pp.car().b;
        YMap<String, JammyObject.PropertyDesc> cdr = pp.cdr();
        YList<YMap<String, Object>> result = al();

        Tuple<Boolean, Object> tuple = car.isMethod == null ? getValue(data, car.name) : car.isMethod ? getMethodValue(data, car.name) : getFieldValue(data, car.name);
        if (!tuple.a) return null;
        for (YMap<String, Object> m : matcher.match(tuple.b, car.value, cur)) {
            result.addAll(matchProps(matcher, data, cdr, m));
        }
        return result;
    }

    private static Tuple<Boolean, Object> getValue(Object o, String name) {
        Field field = getField(o, name);
        if (field != null) return new Tuple<>(true, Reflector.get(o, field));
        try {
            Method method = getMethod(o, name);
            if (method != null) return new Tuple<>(true, method.invoke(o));
        } catch (Exception ignore) {
        }
        return new Tuple<>(false, null);
    }

    private static Tuple<Boolean, Object> getMethodValue(Object o, String name) {
        try {
            Method method = getMethod(o, name);
            if (method != null) return new Tuple<>(true, method.invoke(o));
        } catch (Exception ignore) {
        }
        return new Tuple<>(false, null);
    }

    private static Tuple<Boolean, Object> getFieldValue(Object o, String name) {
        Field field = getField(o, name);
        if (field != null) return new Tuple<>(true, Reflector.get(o, field));
        return new Tuple<>(false, null);
    }

    //OPTIMIZATIONS
    private static final YMap<String, Field> FIELDS = hm();
    private static Field getField(Object o, String name) {
        String key = o.getClass().toString() + ":" + name;
        if (FIELDS.containsKey(key)) return FIELDS.get(key);
        Field result = Reflector.getField(o.getClass(), name);
        FIELDS.put(key, result);
        return result;
    }
    private static final YMap<String, Method> METHODS = hm();
    private static Method getMethod(Object o, String name) {
        String key = o.getClass().toString() + ":" + name;
        if (METHODS.containsKey(key)) return METHODS.get(key);
        Method result = null;
        try {
            result = o.getClass().getMethod(name);
        } catch (NoSuchMethodException ignore) {}
        if (result != null) result.setAccessible(true);
        METHODS.put(key, result);
        return result;
    }


    public static class PropertyDesc {
        public String name;
        public Boolean isMethod;
        public Object value;

        public PropertyDesc(String name, Boolean isMethod, Object value) {
            this.name = name;
            this.isMethod = isMethod;
            this.value = value;
        }
    }

    @Override
    public String toString() {
        return "JammyObject{" +
                "pp=" + pp +
                '}';
    }
}
