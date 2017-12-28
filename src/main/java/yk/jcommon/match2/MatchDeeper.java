package yk.jcommon.match2;

import yk.jcommon.collections.YList;

/**
 * Created with IntelliJ IDEA.
 * User: yuri
 * Date: 31/10/15
 * Time: 11:46
 */
public class MatchDeeper {
    public YList<Object> accessorPatterns;
    public Object rest;

    //TODO assert one var with name "access" (?)
    public MatchDeeper(YList<Object> accessorPatterns, Object rest) {
        this.accessorPatterns = accessorPatterns;
        this.rest = rest;
    }

    public MatchDeeper(YList<Object> accessorPatterns) {
        this.accessorPatterns = accessorPatterns;
    }
}
