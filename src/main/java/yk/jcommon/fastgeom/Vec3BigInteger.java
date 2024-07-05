package yk.jcommon.fastgeom;

import yk.jcommon.utils.BigIntegerMath;

import java.math.BigInteger;
import java.util.Objects;
import java.util.function.Function;

import static yk.jcommon.utils.BigIntegerMath.bi;

public class Vec3BigInteger {
    public BigInteger x, y, z;

    public Vec3BigInteger(long x, long y, long z) {
        this.x = BigInteger.valueOf(x);
        this.y = BigInteger.valueOf(y);
        this.z = BigInteger.valueOf(z);
    }

    public static Vec3BigInteger v3bi(long b) {return v3bi(BigInteger.valueOf(b));}
    public static Vec3BigInteger v3bi(Vec3l from) {return v3bi(bi(from.x), bi(from.y), bi(from.z));}
    public static Vec3BigInteger v3bi(Vec3i from) {return v3bi(bi(from.x), bi(from.y), bi(from.z));}
    public static Vec3BigInteger v3bi(long x, long y, long z) {return v3bi(BigInteger.valueOf(x), BigInteger.valueOf(y), BigInteger.valueOf(z));}

//2022 auto generated text
/*2022*///constructors
/*2022*/public Vec3BigInteger(BigInteger x, BigInteger y, BigInteger z) {this.x = x;this.y = y;this.z = z;}
/*2022*/public Vec3BigInteger(Vec3BigInteger other) {x = other.x;y = other.y;z = other.z;}
/*2022*/public Vec3BigInteger(BigInteger b) {x = b;y = b;z = b;}
/*2022*/public static Vec3BigInteger v3bi(BigInteger x, BigInteger y, BigInteger z) {return new Vec3BigInteger(x, y, z);}
/*2022*/public static Vec3BigInteger v3bi(BigInteger b) {return new Vec3BigInteger(b, b, b);}
/*2022*/public void fillFrom(Vec3BigInteger other) {x = other.x;y = other.y;z = other.z;}
/*2022*///operators
/*2022*/public Vec3BigInteger add(Vec3BigInteger b) {return new Vec3BigInteger(x.add(b.x), y.add(b.y), z.add(b.z));}
/*2022*/public Vec3BigInteger add(BigInteger b) {return new Vec3BigInteger(x.add(b), y.add(b), z.add(b));}
/*2022*/public Vec3BigInteger add(BigInteger x, BigInteger y, BigInteger z) {return new Vec3BigInteger(this.x.add(x), this.y.add(y), this.z.add(z));}
/*2022*/public Vec3BigInteger sub(Vec3BigInteger b) {return new Vec3BigInteger(x.subtract(b.x), y.subtract(b.y), z.subtract(b.z));}
/*2022*/public Vec3BigInteger sub(BigInteger b) {return new Vec3BigInteger(x.subtract(b), y.subtract(b), z.subtract(b));}
/*2022*/public Vec3BigInteger sub(BigInteger x, BigInteger y, BigInteger z) {return new Vec3BigInteger(this.x.subtract(x), this.y.subtract(y), this.z.subtract(z));}
/*2022*/public Vec3BigInteger mul(Vec3BigInteger b) {return new Vec3BigInteger(x.multiply(b.x), y.multiply(b.y), z.multiply(b.z));}
/*2022*/public Vec3BigInteger mul(BigInteger b) {return new Vec3BigInteger(x.multiply(b), y.multiply(b), z.multiply(b));}
/*2022*/public Vec3BigInteger mul(BigInteger x, BigInteger y, BigInteger z) {return new Vec3BigInteger(this.x.multiply(x), this.y.multiply(y), this.z.multiply(z));}
/*2022*/public Vec3BigInteger div(Vec3BigInteger b) {return new Vec3BigInteger(x.divide(b.x), y.divide(b.y), z.divide(b.z));}
/*2022*/public Vec3BigInteger div(BigInteger b) {return new Vec3BigInteger(x.divide(b), y.divide(b), z.divide(b));}
/*2022*/public Vec3BigInteger div(BigInteger x, BigInteger y, BigInteger z) {return new Vec3BigInteger(this.x.divide(x), this.y.divide(y), this.z.divide(z));}
/*2022*/public Vec3BigInteger map(Function<BigInteger, BigInteger> mapper) {return new Vec3BigInteger(mapper.apply(x), mapper.apply(y), mapper.apply(z));}
/*2022*///0 args functions
/*2022*/public Vec3BigInteger abs() {return new Vec3BigInteger(x.abs(), y.abs(), z.abs());}
/*2022*///reducers
/*2022*/public BigInteger sum() {return x.add(y).add(z);}
/*2022*/public BigInteger product() {return x.multiply(y).multiply(z);}
/*2022*/public BigInteger min() {return x.min(y).min(z);}
/*2022*/public BigInteger max() {return x.max(y).max(z);}
/*2022*///distance
/*2022*/public BigInteger lengthSquared() {return x.multiply(x).add(y.multiply(y)).add(z.multiply(z));}
/*2022*/public BigInteger distanceSquared(Vec3BigInteger b) {return BigIntegerMath.sqr(x.subtract(b.x)).add(BigIntegerMath.sqr(y.subtract(b.y))).add(BigIntegerMath.sqr(z.subtract(b.z)));}
/*2022*/public BigInteger distanceSquared(BigInteger x, BigInteger y, BigInteger z) {return BigIntegerMath.sqr(this.x.subtract(x)).add(BigIntegerMath.sqr(this.y.subtract(y))).add(BigIntegerMath.sqr(this.z.subtract(z)));}
/*2022*/public BigInteger manhattanLength() {return x.abs().add(y.abs()).add(z.abs());}
/*2022*/public BigInteger manhattanDistance(Vec3BigInteger b) {return x.subtract(b.x).abs().add(y.subtract(b.y).abs()).add(z.subtract(b.z).abs());}
/*2022*/public BigInteger manhattanDistance(BigInteger x, BigInteger y, BigInteger z) {return this.x.subtract(x).abs().add(this.y.subtract(y).abs()).add(this.z.subtract(z).abs());}
/*2022*///math
/*2022*/public BigInteger dot(Vec3BigInteger b) {return x.multiply(b.x).add(y.multiply(b.y)).add(z.multiply(b.z));}
/*2022*/public BigInteger dot(BigInteger x, BigInteger y, BigInteger z) {return this.x.multiply(x).add(this.y.multiply(y)).add(this.z.multiply(z));}
/*2022*/public BigInteger scalarProduct(Vec3BigInteger b) {return dot(b);}
/*2022*/public Vec3BigInteger negative() {return new Vec3BigInteger(x.negate(), y.negate(), z.negate());}
/*2022*///binary functions
/*2022*/public Vec3BigInteger min(Vec3BigInteger b) {return new Vec3BigInteger(x.min(b.x), y.min(b.y), z.min(b.z));}
/*2022*/public Vec3BigInteger min(BigInteger b) {return new Vec3BigInteger(x.min(b), y.min(b), z.min(b));}
/*2022*/public Vec3BigInteger min(BigInteger x, BigInteger y, BigInteger z) {return new Vec3BigInteger(this.x.min(x), this.y.min(y), this.z.min(z));}
/*2022*/public Vec3BigInteger max(Vec3BigInteger b) {return new Vec3BigInteger(x.max(b.x), y.max(b.y), z.max(b.z));}
/*2022*/public Vec3BigInteger max(BigInteger b) {return new Vec3BigInteger(x.max(b), y.max(b), z.max(b));}
/*2022*/public Vec3BigInteger max(BigInteger x, BigInteger y, BigInteger z) {return new Vec3BigInteger(this.x.max(x), this.y.max(y), this.z.max(z));}
/*2022*/public Vec3BigInteger clamp(Vec3BigInteger min, Vec3BigInteger max) {return new Vec3BigInteger(x.max(min.x).min(max.x), y.max(min.y).min(max.y), z.max(min.z).min(max.z));}
/*2022*/public Vec3BigInteger clamp(BigInteger min, BigInteger max) {return new Vec3BigInteger(x.max(min).min(max), y.max(min).min(max), z.max(min).min(max));}
//2022 auto generated text


    @Override
    public String toString() {
        return "Vec3BigInteger(" + x + ", " + y + ", " + z + ')';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vec3BigInteger that = (Vec3BigInteger) o;
        return Objects.equals(x, that.x) && Objects.equals(y, that.y) && Objects.equals(z, that.z);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }
}