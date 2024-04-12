package yk.jcommon.match2;

import yk.jcommon.utils.BadException;
import yk.ycollections.YList;
import yk.ycollections.YMap;
import yk.ycollections.YSet;

import static yk.ycollections.YArrayList.al;

/**
 * Created with IntelliJ IDEA.
 * User: yuri
 * Date: 12/09/16
 * Time: 09:48
 */
public class MatchOr implements MatchCustomPattern {
    public YList<Object> variants;

    public MatchOr() {
    }

    public MatchOr(Object... variants) {
        if (variants.length < 2) BadException.die("Variants size should be > 1");
        this.variants = al(variants);
    }

    public MatchOr setVariants(YList<Object> variants) {
        if (variants.size() < 2) BadException.die("Variants size should be > 1");
        this.variants = variants;
        return this;
    }

    @Override
    public YSet<YMap<String, Object>> match(Matcher matcher, Object data, YMap<String, Object> cur) {
        return variants.flatMap(v -> matcher.match(data, v, cur)).toSet();
    }

    @Override
    public String toString() {
        return "MatchOr{" +
                "variants=" + variants +
                '}';
    }
}
