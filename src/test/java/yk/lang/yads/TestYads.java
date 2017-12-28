package yk.lang.yads;

import org.junit.Test;
import yk.jcommon.collections.Tuple;
import yk.jcommon.fastgeom.Vec2f;
import yk.jcommon.utils.BadException;
import yk.jcommon.utils.Reflector;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.fail;
import static yk.jcommon.collections.YArrayList.al;
import static yk.jcommon.collections.YHashMap.hm;

/**
 * Created with IntelliJ IDEA.
 * User: yuri
 * Date: 08/02/15
 * Time: 16:40
 */
public class TestYads {

    @Test
    public void parsePrimitives() {
        assertEquals(al("hello", "hello", "hello", "", ""), YadsParser.parseList("hello 'hello' \"hello\" '' \"\""));
        assertEquals(al("\"hello\"", "'hello'"), YadsParser.parseList(" '\\\"hello\\\"'  '\\'hello\\''"));
        assertEquals(al("hello\nworld", "hello\tworld"), YadsParser.parseList(" 'hello\\nworld'  'hello\\tworld'"));

        assertEquals(al("hello\nworld", "hello\tworld"), YadsParser.parseList(" 'hello\nworld'  'hello\tworld'"));

        assertEquals(al("\"'hello world'\"", "\"'hello world'\""), YadsParser.parseList(" \"\\\"'hello world'\\\"\"  '\"\\'hello world\\'\"'"));
        assertEquals(al("\\'"), YadsParser.parseList(" \"\\\\'\" "));
        assertEquals(al("\\\""), YadsParser.parseList(" '\\\\\"' "));

        assertEquals(al(10, 10l, 10L, 10f, 10d, 10D, 10.1d, 10.1f, 10.1f), YadsParser.parseList("10 10l 10L 10f 10d 10D 10.1d 10.1f 10.1"));
        assertEquals(al(1424083792130l), YadsParser.parseList("1424083792130l"));
        assertEquals(al(-10), YadsParser.parseList("-10"));
        assertEquals(al(true, false), YadsParser.parseList("true false"));
    }

    @Test
    public void serializePrimitives() {
        assertEquals("10l", YadsSerializer.serialize(10l));
        assertEquals("hello", YadsSerializer.serialize("hello"));
        assertEquals("'hello\\n'", YadsSerializer.serialize("hello\n"));//TODO don't escape?
    }

    @Test
    public void deserializer() {
        assertEquals(al("hello", "world"), YadsSerializer.deserialize("hello world"));
        assertEquals(al(al("hello", "world"), al("hello2", "world2")), YadsSerializer.deserialize("{hello world} {hello2 world2}"));
        assertEquals(hm("hello", "world"), YadsSerializer.deserialize("hello=world"));

        assertEquals(new YadsClass(null, al(new Tuple("a", "b"), "c")), YadsSerializer.deserialize("a=b c"));
        assertEquals(al(new YadsClass(null, al(new Tuple("a", "b"), "c"))), YadsSerializer.deserialize("{a=b c}"));
        assertEquals(al(new YadsClass("name", al(new Tuple("a", "b"), "c"))), YadsSerializer.deserialize("name{a=b c}"));

        assertEquals(al(new TestEnumClass(TestEnum.ENUM1)), YadsSerializer.deserialize(Namespaces.packages("yk.lang.yads"), "TestEnumClass{enumField=ENUM1}"));
        assertEquals(al(TestEnum.ENUM1), YadsSerializer.deserialize(Namespaces.packages("yk.lang.yads"), "TestEnum{ENUM1}"));

        assertEquals(al(new TestEnumClass(null)), YadsSerializer.deserialize(Namespaces.packages("yk.lang.yads"), "TestEnumClass{enumField=null}"));
    }

    @Test
    public void deserializerImports() {
        assertEquals(al(new YadsClass("XY", al(1, 2))), YadsSerializer.deserialize("XY{1 2}"));
        assertEquals(al(new Vec2f(1, 2)), YadsSerializer.deserialize("yk.jcommon.fastgeom.Vec2f{x=1 y=2}"));
        assertEquals(al(new Vec2f(1, 2)), YadsSerializer.deserialize("import=yk.jcommon.fastgeom \n\n Vec2f{x=1 y=2}"));
        assertEquals(new TestClass(al(1, 2), hm("key1", "value1", "key2", "value2"), 3), YadsSerializer.deserializeClass(TestClass.class, "import=yk.jcommon.fastgeom, yk.jcommon.fastgeom someList=1, 2 someMap={key1=value1 'key2'=value2} someInt=3"));
    }

    @Test
    public void serializer() {
        assertEquals("import= yk.jcommon.fastgeom\n\nVec2f{x= 1.0 y= 2.0}", YadsSerializer.serialize(new Vec2f(1, 2)));
        assertEquals("{\n  hello\n  world\n}\n", YadsSerializer.serialize((Object)al("hello", "world")));
        assertEquals("{\n  k= v\n}\n", YadsSerializer.serialize(hm("k", "v")));

        assertEquals("import= yk.lang.yads\n\nTestEnumClass{enumField= ENUM1}", YadsSerializer.serialize(new TestEnumClass(TestEnum.ENUM1)));
        assertEquals("import= yk.lang.yads\n\nTestEnumClass{}", YadsSerializer.serialize(new TestEnumClass(null)));

        assertEquals("{\n  hello\n  null\n}\n", YadsSerializer.serialize((Object)al("hello", null)));

        assertEquals("{\n  'h\"e\\'l\\nl\\to'\n}\n", YadsSerializer.serialize((Object)al("h\"e'l\nl\to")));

        assertEquals("enumField= ENUM1\n", YadsSerializer.serializeInner(new TestEnumClass(TestEnum.ENUM1)));
    }

    @Test
    public void testClass() {
        assertEquals(new TestClass(al(1, 2), hm("key1", "value1", "key2", "value2"), 3), YadsSerializer.deserializeClass(TestClass.class, "someList=1, 2 someMap={key1=value1 'key2'=value2} someInt=3"));

        TestClass test1 = new TestClass();
        test1.someList2 = al(1, 2);
        test1.someList3 = al(3, 4);
        assertEquals(test1, YadsSerializer.deserializeClass(TestClass.class, "someList2=1, 2 someList3=3,4"));

        assertEquals(new TestClass(al(1, 2), hm("key1", "value1", "key2", "value2"), 3), YadsSerializer.deserializeClass(TestClass.class, "1, 2 {key1=value1 'key2'=value2} 3"));

        assertEquals(new TestClass(false), YadsSerializer.deserializeClass(TestClass.class, "someBoolean=false"));

        try {
            assertEquals(new TestClass(al(1, 2), hm("key1", "value1", "key2", "value2"), 3), YadsSerializer.deserializeClass(TestClass.class, "someList=1, 2 someMap=hello someInt=3"));
            fail();
        } catch (BadException ignore) {
            assertEquals("found instance hello of class class java.lang.String but expected object of class yk.jcommon.collections.YHashMap", ignore.getMessage());
        }

        TestClass tc = new TestClass();
        tc.tc2 = new TestClass2(1);
        assertEquals(tc, YadsSerializer.deserializeClass(TestClass.class, "tc2=1f, 1f"));
        //assertEquals(tc, YadsSerializer.deserializeClass(TestClass.class, "tc2={1f, 1f}"));
        //assertEquals(tc, YadsSerializer.deserializeClass(TestClass.class, "tc2=TestClass2{1f, 1f}"));
        assertEquals(tc, YadsSerializer.deserializeClass(TestClass.class, "tc2=1f"));
        assertEquals(tc, YadsSerializer.deserializeClass(TestClass.class, "tc2={1f}"));
        assertEquals(tc, YadsSerializer.deserializeClass(TestClass.class, "tc2=TestClass2{1f}"));

        tc.tc2 = new TestClass2(al("hello", "world"));
        tc.tc2.a = 4;
        TestClass des = YadsSerializer.deserializeClass(TestClass.class, "tc2={a=4 hello world}");
        assertEquals(tc, des);
    }

    @Test
    public void testYadsAware() {
        assertEquals("import= yk.lang.yads\n\nTestClass2{1.0}", YadsSerializer.serialize(new TestClass2(1)));
        assertEquals("import= yk.lang.yads\n\nTestClass2{1.0 2.0}", YadsSerializer.serialize(new TestClass2(1, 2)));

        TestClass tc = new TestClass();
        tc.tc2 = new TestClass2(1);
        assertEquals("import= yk.lang.yads\n\nTestClass{someInt= 0 tc2={1.0}someBoolean= false}", YadsSerializer.serialize(tc));

    }

    @Test
    public void testFieldTypeNotImported() {
        assertEquals(fill(new TestClass3(), "pos", new Vec2f(3, 3)), ((List)YadsSerializer.deserialize("import=yk.lang.yads TestClass3{pos=3f, 3f}")).get(0));
        assertEquals(fill(new TestClass3(), "pos", new Vec2f(3, 3)), YadsSerializer.deserializeClass(TestClass3.class, "pos=3f, 3f"));
    }

    @Test
    public void testFieldTypeIsKnown() {
        assertEquals("import= yk.lang.yads\n\nTestClass3{pos={x= 3.0 y= 3.0}}", YadsSerializer.serialize(fill(new TestClass3(), "pos", new Vec2f(3, 3))));
    }

    private static <T> T fill(T obj, Object... values) {
        for (int i = 0; i < values.length; i += 2) Reflector.set(obj, (String) values[i], values[i+1]);
        return obj;
    }

    @Test
    public void test() {
        System.out.println("'\u005cn'");

        System.out.println(YadsParser.parseList("hello world"));
        System.out.println(YadsParser.parseClass("XY{10 20}"));
        System.out.println(YadsParser.parseClass("HBox{pos = 10, 20 VBox{size= 50, 50}}"));
        System.out.println(YadsSerializer.deserialize("import=yk.lang.yads HBox{pos = 10, 20 VBox{size= 50, 50}}"));
        System.out.println(HBox.class.getName());
        //System.out.println(YADSSerializer.deserializeClass(null, YadsParser.parseClass("HBox{pos = 10, 20}")).toStringPrefixInfix());

        //System.out.println(YADSSerializer.deserializeList(YadsParser.parseList("import= yk.lang.yads HBox{pos = 10, 20}")).toStringPrefixInfix());

        //TODO convert with respect to method call arguments types!
        //TODO map or YAD if class not defined and unknown

    }

    @Test
    public void testDeserializeComfort() {
        System.out.println(YadsSerializer.deserializeMap("import=yk.lang.yads hello=world someOther={}"));

        System.out.println(YadsSerializer.deserializeList("import=yk.lang.yads hello world {}"));
        System.out.println(YadsSerializer.deserializeList("import=yk.lang.yads hello world {hello=world}"));
        System.out.println(YadsSerializer.deserializeList("import=yk.lang.yads hello world {hello}"));
        System.out.println(YadsSerializer.deserializeList("import=yk.lang.yads hello world {hello world}"));
        System.out.println(YadsSerializer.deserializeList("import=yk.lang.yads hello world {hello=world someother}"));

        System.out.println(YadsSerializer.deserializeNamedMap("import=yk.lang.yads world {hello=world}"));

        System.out.println(YadsSerializer.deserialize("import=yk.lang.yads someOther {hello=world}"));
        System.out.println(YadsSerializer.deserialize("import=yk.lang.yads someOther hello=world"));
        System.out.println(YadsSerializer.deserialize("import=yk.lang.yads hello=world someOther"));

    }


}
