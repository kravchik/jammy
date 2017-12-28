package yk.lang.yads;

import yk.jcommon.collections.Tuple;
import yk.jcommon.collections.YList;
import yk.jcommon.collections.YMap;

import static yk.jcommon.collections.YHashMap.hm;

/**
 * Created with IntelliJ IDEA.
 * User: yuri
 * Date: 05/02/15
 * Time: 08:56
 */
public class YadsClass {
    public String name;
    public YList body;

    public YadsClass(String s, YList l) {
        name = s;
        body = l;
    }

    @Override
    public String toString() {
        return "YadsClass{" +
                "name='" + name + '\'' +
                ", body=" + body +
                '}';
    }

    public YMap getProperties() {
        YMap result = hm();
        for (Object tuple : body.filter(element -> element instanceof Tuple)) {
            result.put(((Tuple)tuple).a, ((Tuple)tuple).b);
        }
        return result;
    }

    public YList getList() {
        return body.filter(element -> !(element instanceof Tuple));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        YadsClass yadClass = (YadsClass) o;

        if (!body.equals(yadClass.body)) return false;
        if (name != null ? !name.equals(yadClass.name) : yadClass.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + body.hashCode();
        return result;
    }
}
