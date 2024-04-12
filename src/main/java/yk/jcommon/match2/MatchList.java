package yk.jcommon.match2;

import yk.ycollections.Tuple;
import yk.ycollections.YList;
import yk.ycollections.YMap;
import yk.ycollections.YSet;

import java.util.List;

import static yk.ycollections.YArrayList.al;
import static yk.ycollections.YArrayList.toYList;
import static yk.ycollections.YHashSet.hs;

/**
 * Created with IntelliJ IDEA.
 * User: yuri
 * Date: 28/08/16
 * Time: 11:14
 */
public class MatchList implements MatchCustomPattern {//TODO rename?
    public YList pattern;

    public MatchList(YList pattern) {
        this.pattern = pattern;
    }

    @Override
    public YSet<YMap<String, Object>> match(Matcher matcher, Object data, YMap<String, Object> cur) {
        if (!(data instanceof List)) return hs();
        //TODO implement with fillers ?
        return matchRest2(matcher, data instanceof YList ? (YList) data : toYList((List) data), this.pattern, cur);
        //return matchRest(matcher, data instanceof YList ? (YList) data : toYList((List) data), this.pattern, cur);
    }

    public static YSet<YMap<String, Object>> matchRest(Matcher matcher, YList data, YList pattern, YMap<String, Object> cur) {
        if (pattern.isEmpty() && data.isEmpty()) return hs(cur);
        if (pattern.isEmpty()) return hs();
        Object p = pattern.car();

        if (p instanceof Filler) {
            Filler f = (Filler) p;
            YSet<YMap<String, Object>> result = hs();
            int max = data.size();
            if (f.maxLength != null && f.maxLength < max) max = f.maxLength;
            int min = f.minLength;

            //is this optimization necessary?
            if (pattern.size() == 1) {//if filler is last, it must fill all the rest
                if (data.size() < min) return hs();//already not enough data
                min = data.size();
            }

            for (int i = min; i < max + 1; i++) {
                //"back filling"
                YSet<YMap<String, Object>> restMatches = matchRest(matcher, data.subList(i, data.size()), pattern.cdr(), cur);
                YList forFiller = null;
                for (YMap<String, Object> restMatch : restMatches) {
                    if (forFiller == null) forFiller = data.subList(0, i);
                    if (f.inside == null) result.add(restMatch);
                    else result.addAll(matcher.match(forFiller, f.inside, restMatch));
                }
            }
            return result;
        } else {
            if (data.isEmpty()) return hs();
            YSet<YMap<String, Object>> result = hs();
            YList dCdr = data.cdr();
            YList pCdr = pattern.cdr();
            for (YMap<String, Object> map : matcher.match(data.car(), p, cur)) {
                result.addAll(matchRest(matcher, dCdr, pCdr, map));
            }
            return result;
        }
    }

    public static YSet<YMap<String, Object>> mix(YSet<YMap<String, Object>> curr, YSet<YMap<String, Object>> newMap) {
        YSet<YMap<String, Object>> result = hs();
        a:for (YMap<String, Object> nm : newMap) {
            for (YMap<String, Object> cur : curr) {
                for (String k : nm.keySet()) {
                    if (nm.containsKey(k) && !cur.get(k).equals(nm.get(k))) {
                        continue a;
                    }
                }
                result.add(cur.with(nm));
            }
        }
        return result;
    }

    public static YMap<String, Object> mixMap(YMap<String, Object> cur, YMap<String, Object> nm) {
        for (String k : nm.keySet()) {
            if (cur.containsKey(k) && !cur.get(k).equals(nm.get(k))) {
                return null;
            }
        }
        return cur.with(nm);
    }


    public static YSet<YMap<String, Object>> matchRest2(Matcher matcher, YList data, YList pattern, YMap<String, Object> cur) {
        //плоский поиск, с новым под-матчингом

        YList<Tuple<Integer, YMap<String, Object>>> state = al(new Tuple<>(-1, cur));

        for (int pIndex = 0; pIndex < pattern.size(); pIndex++) {
            Object p = pattern.get(pIndex);
            Filler prevFiller = null;
            if (pIndex > 0 && pattern.get(pIndex - 1) instanceof Filler && ((Filler)(pattern.get(pIndex - 1))).inside != null) prevFiller = (Filler) pattern.get(pIndex - 1);


            if (p instanceof Filler) {
                Filler filler = (Filler) p;
                //TODO assert we don't have 2+ fillers in a row
                //TODO implement 2+ fillers in a row

                if (filler == pattern.last() && filler.inside != null) {
                    YSet<YMap<String, Object>> result = hs();
                    for (Tuple<Integer, YMap<String, Object>> tuple : state) {
                        YSet<YMap<String, Object>> match = matcher.match(data.subList(tuple.a + 1, data.size()), filler.inside, tuple.b);
                        for (YMap<String, Object> map : match) result.add(map);
                    }
                    return result;
                }


            } else {
                YList<Tuple<Integer, YMap<String, Object>>> newState = al();

                //TODO look at possible tuple starts and their maps
                for (int dIndex = 0; dIndex < data.size(); dIndex++) {
                    Object d = data.get(dIndex);
                    YSet<YMap<String, Object>> mm = matcher.match(d, p);


                    for (YMap<String, Object> m : mm) {
                        for (Tuple<Integer, YMap<String, Object>> tuple : state) {
                            if (tuple.a < dIndex) {
                                YMap<String, Object> mix = mixMap(tuple.b, m);
                                if (mix == null) continue;

                                if (prevFiller != null) {
                                    YSet<YMap<String, Object>> match = matcher.match(data.subList(tuple.a + 1, dIndex), prevFiller.inside, mix);
                                    for (YMap<String, Object> map : match) newState.add(new Tuple(dIndex, map));
                                } else newState.add(new Tuple(dIndex, mix));
                            }
                        }
                    }
                }
                state = newState;
            }
        }
        return state.map(t -> t.b).toSet();
    }

    public static class Filler {
        public Object inside;
        public int minLength = 0;
        public Integer maxLength;

        public Filler setMaxLength(int maxLength) {
            this.maxLength = maxLength;
            return this;
        }

        public Filler setMinLength(int minLength) {
            this.minLength = minLength;
            return this;
        }

        public Filler setInside(Object inside) {
            this.inside = inside;
            return this;
        }
    }
}
