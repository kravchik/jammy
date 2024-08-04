package yk.jammy;

import yk.jammy.utils.BadException;
import yk.ycollections.YList;
import yk.ycollections.YMap;

import static yk.ycollections.YArrayList.al;

/**
 * Created with IntelliJ IDEA.
 * User: yuri
 * Date: 12/09/16
 * Time: 09:48
 */
public class JammyOr implements JammyCustomPattern<JammyOr> {
    public YList<Object> variants;

    public JammyOr() {
    }

    public JammyOr(YList<Object> variants) {
        this.variants = variants;
    }

    public JammyOr setVariants(YList<Object> variants) {
        if (variants.size() < 2) BadException.die("Variants size should be > 1");
        this.variants = variants;
        return this;
    }

    @Override
    public YList<YMap<String, Object>> match(JammyMatcher matcher, Object data, YMap<String, Object> cur) {
        return variants.flatMap(v -> matcher.match(data, v, cur));
    }

    @Override
    public String toString() {
        return "JammyOr{" +
                "variants=" + variants +
                '}';
    }
}
