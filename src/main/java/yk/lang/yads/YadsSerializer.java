package yk.lang.yads;

import org.apache.commons.lang3.text.translate.*;
import yk.jcommon.collections.Tuple;
import yk.jcommon.collections.YList;
import yk.jcommon.collections.YMap;
import yk.jcommon.collections.YSet;
import yk.jcommon.utils.BadException;
import yk.jcommon.utils.Reflector;
import yk.jcommon.utils.Tab;
import yk.jcommon.utils.Util;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import static yk.jcommon.collections.YArrayList.al;
import static yk.jcommon.collections.YHashMap.hm;
import static yk.jcommon.collections.YHashSet.hs;

/**
 * Created with IntelliJ IDEA.
 * User: yuri
 * Date: 05/02/15
 * Time: 23:22
 */
public class YadsSerializer {
    public static int compactWidth = 100;
    //private static YList<String> namespaces = al("", "test", "yk.lang.yads");
    private static Tab tab = new Tab("  ");

    public static final CharSequenceTranslator UNESCAPE_YADS_SINGLE_QUOTES =
            new AggregateTranslator(
                    new OctalUnescaper(),     // .between('\1', '\377'),
                    new UnicodeUnescaper(),
                    new LookupTranslator(EntityArrays.JAVA_CTRL_CHARS_UNESCAPE()),
                    new LookupTranslator(
                            new String[][] {
                                    {"\\\\", "\\"},
                                    {"\\'", "'"},
                                    {"\\", ""}
                            })
            );

    public static final CharSequenceTranslator UNESCAPE_YADS_DOUBLE_QUOTES =
            new AggregateTranslator(
                    new OctalUnescaper(),     // .between('\1', '\377'),
                    new UnicodeUnescaper(),
                    new LookupTranslator(EntityArrays.JAVA_CTRL_CHARS_UNESCAPE()),
                    new LookupTranslator(
                            new String[][] {
                                    {"\\\\", "\\"},
                                    {"\\\"", "\""},
                                    {"\\", ""}
                            })
            );

    public static final AggregateTranslator ESCAPE_YADS_SINGLE_QUOTES = new AggregateTranslator(new LookupTranslator(new String[][]{{"'", "\\'"}, {"\\", "\\\\"}}), new LookupTranslator(EntityArrays.JAVA_CTRL_CHARS_ESCAPE()));
    public static final AggregateTranslator ESCAPE_YADS_DOUBLE_QUOTES = new AggregateTranslator(new LookupTranslator(new String[][]{{"\"", "\\\""}, {"\\", "\\\\"}}), new LookupTranslator(EntityArrays.JAVA_CTRL_CHARS_ESCAPE()));

    public static String serialize(Object o) {
        YSet<String> namespaces = hs();
        String result = serialize(namespaces, false, o);
        return serializeNamespaces(namespaces) + result;
    }

    public static String serializeList(List o) {
        YSet<String> namespaces = hs();
        String result = serializeListImpl(namespaces, o);
        return serializeNamespaces(namespaces) + result;
    }

    public static String serializeMap(Map o) {
        YSet<String> namespaces = hs();
        String result = serializeMapImpl(namespaces, o);
        return serializeNamespaces(namespaces) + result;
    }

    public static String serializeClassBody(Object o) {
        YSet<String> namespaces = hs();
        String result = serializeClassBody(namespaces, o);
        return serializeNamespaces(namespaces) + result;
    }

    public static String serializeNamespaces(YSet<String> namespaces) {
        if (namespaces.size() == 0) return "";
        //can't make imports without {}, because last one is parsed as class name if there is list or map going next
        //if (namespaces.size() == 1) return "import=" + namespaces.first() + "\n\n";
        return "import={" + Util.join(namespaces, " ") + "}\n\n";
        //return (namespaces.isEmpty() ? "" : "import= " + Util.join(namespaces, ", ") + "\n\n") + result;
    }

    private static boolean between(char x ,char min, char max) {
        return x >= min && x <= max;
    }

    private static String serialize(YSet<String> namespaces, boolean typeIsKnown, Object o) {
        if (o == null) return "null";
        if (o instanceof Long) return o + "l";
        if (o instanceof Double) return o + "d";
        if (o instanceof Number) return o + "";
        if (o instanceof String) {
            String s = (String) o;
            boolean withoutQuotes = true;
            if (s.length() > 0) {
                for (int i = 0; i < s.length(); i++) {
                    char c = s.charAt(i);
                    if (!(between(c, '0', '9')
                    || between(c, 'A', 'Z')
                    || between(c, 'a', 'z')
                    || between(c, '_', '_'))) withoutQuotes = false;
                }
                if (between(s.charAt(0), '0', '9')) withoutQuotes = false;
            } else {
                withoutQuotes = false;
            }
            if (withoutQuotes) return s;
            return "'" + ESCAPE_YADS_SINGLE_QUOTES.translate((String) o) + "'";//TODO don't escape ' for " and vice versa?
        }
        if (o instanceof Boolean) return o + "";
        if (o instanceof List) return serializeList(namespaces, (List) o);
        if (o instanceof Map) return serializeMap(namespaces, (Map) o);

        if (o instanceof YadsAwareConstructor) {

            List serialized = ((YadsAwareConstructor) o).genConstructorArguments();
            if (serialized == null) return serializeClass(namespaces, o, typeIsKnown);
            String result = (typeIsKnown ? "" : addImport(namespaces, o)) + " {\n";
            result += serializeListImpl(namespaces, serialized);
            result += tab + "}\n";
            return possiblyCompact(result);
        }

        if (o.getClass().isEnum()) return "" + o;
        if (o.getClass().isArray()) {
            if (o.getClass().getComponentType().isArray()) {
                String result = "";
                result += "{\n";
                tab.inc();
                int length = Array.getLength(o);
                for (int i = 0; i < length; i++) result += tab + possiblyCompact(serialize(namespaces, false, Array.get(o, i)));
                tab.dec();
                result += tab + "}\n";
                return result;

            } else {
                String result = "";
                result += "{";
                int length = Array.getLength(o);
                tab.inc();//just in case there will be complex structures
                for (int i = 0; i < length; i++) result += (i > 0 ? " " : "") + possiblyCompact(serialize(namespaces, false, Array.get(o, i)));
                tab.dec();
                result += "}\n";
                return result;
            }
        }
        return possiblyCompact(serializeClass(namespaces, o, typeIsKnown));
    }

    private static String serializeMap(YSet<String> namespaces, Map o) {//TODO add specific Map type if not just HashMap or YHashMap
        String result = "";
        result += "{\n";
        tab.inc();
        result += serializeMapImpl(namespaces, o);
        tab.dec();
        result += tab + "}\n";
        return result;
    }

    private static String serializeMapImpl(YSet<String> namespaces, Map o) {
        String result = "";
        for (Object key : o.keySet()) result += tab + serialize(namespaces, false, key) + "= " + possiblyCompact(serialize(namespaces, false, o.get(key))) + "\n";
        return result;
    }

    private static String serializeList(YSet<String> namespaces, List o) {//TODO add specific List type if not just ArrayList or YArrayList
        String result = "";
        result += "{\n";
        result += serializeListImpl(namespaces, o);
        result += tab + "}\n";
        return result;
    }

    private static String serializeListImpl(YSet<String> namespaces, List o) {
        String result = "";
        tab.inc();
        for (Object el : o) result += tab + possiblyCompact(serialize(namespaces, false, el)) + "\n";
        tab.dec();
        return result;
    }

    private static String possiblyCompact(String s) {
        String compacted = compact(s);
        if (compacted.length() < compactWidth) return compacted;
        return s;
    }

    private static String serializeClass(YSet<String> namespaces, Object o, boolean typeIsKnown) {
        String result = "";
        if (!typeIsKnown) result += addImport(namespaces, o);
        result += " {\n";
        tab.inc();
        result += serializeClassBody(namespaces, o);
        tab.dec();
        result += tab + "}\n";
        return result;
    }

    private static String addImport(YSet<String> namespaces, Object o) {
        String name = o.getClass().getName();
        if (name.contains(".")) {
            namespaces.add(getPackageName(name));
            name = name.substring(name.lastIndexOf(".") + 1);
        }
        return name;
    }

    private static String getPackageName(String name) {
        return name.substring(0, name.lastIndexOf("."));
    }

    private static String serializeClassBody(YSet<String> namespaces, Object o) {
        String result = "";
        for (Field field : Reflector.getAllNonStaticFieldsReversed(o.getClass()).values()) {
            Object value = Reflector.get(o, field);
            if (value == null) continue;//TODO other defaults
            result += tab + field.getName() + "= " + possiblyCompact(serialize(namespaces, field.getType() == value.getClass(), value)) + "\n";
        }
        return result;
    }

    public static YMap deserializeMap(String s) {
        Object result = deserialize2(s);
        if (result instanceof List && ((List) result).isEmpty()) return hm();
        return (YMap) result;
    }

    public static NamedMap deserializeNamedMap(String s) {
        Object result = deserialize2(s);
        if (!(result instanceof List) || ((List) result).size() != 1) BadException.die("wrong syntax inside (expected \"name{key=value}\")");
        Object m = ((List) result).get(0);
        if (!(m instanceof NamedMap)) BadException.die("wrong syntax inside (expected \"name{key=value}\")");
        return (NamedMap) m;
    }

    public static YList deserializeList(String s) {
        return (YList) deserialize2(s);
    }

    private static Object deserialize2(String s) {
        Namespaces namespaces = new Namespaces();
        namespaces.enterScope();
        namespaces.addPackage("");
        return deserialize(namespaces, s);
    }


    public static Object deserialize(String s) {
        Namespaces namespaces = new Namespaces();
        namespaces.enterScope();
        namespaces.addPackage("");
        Object result = deserialize(namespaces, s);
        if (!(result instanceof List)) BadException.die("bad structure on top");
        if (((List)result).size() != 1) BadException.die("expected list with strictly one element on top");

        return ((List)result).get(0);
    }

    public static Object deserialize(Namespaces namespaces, String s) {
        return _deserializeList(namespaces, YadsParser.parseList(s));
    }

    public static <T> T deserializeClassBody(Class<? extends T> clazz, String s) {
        Namespaces namespaces = new Namespaces();
        namespaces.enterScope();
        namespaces.addPackage("");
        namespaces.addPackage(getPackageName(clazz.getName()));
        return (T) deserializeClassBody(namespaces, clazz, s);
    }

    public static <T> T deserializeClassBody(Namespaces namespaces, Class<? extends T> clazz, String s) {
        return (T) deserializeClassBody(namespaces, clazz, new YadsClass(null, YadsParser.parseList(s)));
    }

    public static Object _deserializeList(Namespaces namespaces, YList l) {
        return deserializeClassBody(namespaces, null, new YadsClass(null, l));
    }

    private static Object deserializeClassBody(Namespaces namespaces, Object yad) {
        return deserializeClassBody(namespaces, null, yad);
    }

    private static Object deserializeClassBody(Namespaces namespaces, Class clazz, Object yad) {
        if (yad == null) return null;
        if (clazz != null && clazz.isArray()) return parseArray(namespaces, clazz, (YadsClass) yad);
        if (clazz != null && clazz.isEnum()) return Enum.valueOf(clazz, (String) yad);
        if (clazz == Integer.class || clazz == int.class) return ((Number) yad).intValue();
        if (clazz == Float.class || clazz == float.class) return ((Number) yad).floatValue();
        if (clazz == Long.class || clazz == long.class) return ((Number) yad).longValue();
        if (clazz == Double.class || clazz == double.class) return ((Number) yad).doubleValue();
        if (clazz == Boolean.class || clazz == boolean.class) {
                //noinspection UnnecessaryUnboxing
                return ((Boolean) yad).booleanValue();
        }
        if (yad instanceof YadsClass) return returnWithAssert(clazz, deserializeClassImpl(namespaces, clazz, (YadsClass) yad));
        else if (clazz != null && !(Map.class.isAssignableFrom(clazz))) {
            return returnWithAssert(clazz, deserializeClassImpl(namespaces, clazz, new YadsClass(null, al(yad))));
        }
        else return returnWithAssert(clazz, yad);
    }

    private static Object returnWithAssert(Class clazz, Object instance) {
        if (instance == null) return null;
        if (clazz == boolean.class && instance.getClass() == Boolean.class) return instance;
        if (clazz != null) {
            if (!clazz.isAssignableFrom(instance.getClass())) {
                BadException.die("found instance " + instance + " of class " + instance.getClass() + " but expected object of " + clazz);
            }
        }
        return instance;
    }

    private static Object parseArray(Namespaces namespaces, Class clazz, YadsClass yad) {
        Object result = Array.newInstance(clazz.getComponentType(), yad.body.size());
        for (int i = 0; i < yad.body.size(); i++) Array.set(result, i, deserializeClassBody(namespaces, clazz.getComponentType(), yad.body.get(i)));
        return result;
    }

    private static Object deserializeClassImpl(Namespaces namespaces, Class clazz, YadsClass yad) {
        if (yad.name != null) clazz = namespaces.findClass(yad.name);
        //TODO assert found class by class name, extends field?
        YList array = al();
        YList<Tuple> tuples = al();
        namespaces.enterScope();
        if (clazz != null) {

        }
        for (Object element : yad.body) {
            if (element instanceof Tuple) {
                Tuple<String, Object> t = (Tuple<String, Object>) element;
                if ("import".equals(t.a)) {
                    Object value = deserializeClassBody(namespaces, null, t.b);
                    if (value instanceof String) namespaces.addPackage((String) value);
                    else if (value instanceof YList) for (Object o : (YList) value) namespaces.addPackage((String) o);
                    else BadException.die("unknown data " + value + " for import");
                } else {
                    Object value;
                    if (clazz == null || Map.class.isAssignableFrom(clazz)) {
                        value = deserializeClassBody(namespaces, null, t.b);
                    } else {
                        Field field = Reflector.getField(clazz, t.a);
                        if (field == null) throw BadException.die("can't find field " + t.a + " for " + clazz);
                        value = deserializeClassBody(namespaces, field.getType(), t.b);
                    }
                    tuples.add(new Tuple(t.a, value));
                }
            } else {
                array.add(deserializeClassBody(namespaces, null, element));
            }
        }
        if (clazz != null && clazz.isEnum()) {
            if (!tuples.isEmpty()) BadException.die("enum can't contain tuples");
            if (array.size() != 1) BadException.die("enum must be stated by one element");
            namespaces.exitScope();
            return Enum.valueOf(clazz, (String)array.get(0));
        }
        Object instance;

        if (clazz == null) {
            if (yad.name == null) {
                if (array.isEmpty() && tuples.isEmpty()) clazz = List.class;
                else if (!array.isEmpty() && tuples.isEmpty()) clazz = List.class;
                else if (array.isEmpty() && !tuples.isEmpty()) clazz = Map.class;
                else clazz = YadsClass.class;
            } else {
                if (array.isEmpty()) clazz = NamedMap.class;
                else clazz = YadsClass.class;
            }
        }

        if (List.class.isAssignableFrom(clazz)) {
            if (!tuples.isEmpty()) BadException.die("list class '" + clazz + "' cannot be instantiated with tuples");//TODO line number
            if (clazz == List.class) instance = al();
            else if (clazz == YList.class) instance = al();
            else instance = Reflector.newInstance(clazz);
            ((List) instance).addAll(array);
        } else if (Map.class.isAssignableFrom(clazz)) {
            if (!array.isEmpty()) BadException.die("map class '" + clazz + "' cannot be instantiated with list");//TODO line number
            if (clazz == Map.class) instance = hm();
            else if (clazz == YMap.class) instance = hm();
            else instance = Reflector.newInstance(clazz);
            for (Tuple t : tuples) ((Map) instance).put(t.a, t.b);
        } else if (clazz == NamedMap.class) {
            instance = new NamedMap(yad.name);
            for (Tuple t : tuples) ((NamedMap)instance).map.put(t.a, t.b);
        } else if (clazz == YadsClass.class) {
            if (array.isEmpty()) {
                instance = new NamedMap(yad.name);
                for (Tuple t : tuples) ((NamedMap)instance).map.put(t.a, t.b);
            } else {
                instance = new YadsClass(yad.name, tuples.with(array));
            }
        } else if (clazz == Object.class) {
            if (tuples.isEmpty() && array.notEmpty()) return array;
            if (tuples.notEmpty() && array.isEmpty()) {
                YMap result = hm();
                for (Tuple t : tuples) result.put(t.a, t.b);
                return result;
            }
            return new Object();
        } else {
            if (!array.isEmpty()) {
                Constructor constructor = Reflector.getApropriateConstructor(clazz, array.toArray());
                if (constructor != null) instance = Reflector.newInstance(clazz, array.toArray());
                else instance = Reflector.newInstance(clazz, array);
            }
            else instance = Reflector.newInstanceArgless(clazz);
            for (Tuple t : tuples) Reflector.set(instance, (String) t.a, t.b);
        }
        namespaces.exitScope();
        return instance;
    }

    public static String compact(String s) {
        String oldS = "";
        while (s.length() != oldS.length()) {
            oldS = s;
            s = s.replace("\n", " ");
            s = s.replace("  ", " ");
            s = s.replaceAll("\\} (.)", "}$1");
            s = s.replaceAll("\\{ (.)", "{$1");
            s = s.replaceAll("(.) \\}", "$1}");
            s = s.replaceAll("(.) \\{", "$1{");
        }
        return s.trim();
    }
}
