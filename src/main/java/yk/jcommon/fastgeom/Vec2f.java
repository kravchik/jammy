/**
 * File Vec2f.java
 * @author Yuri Kravchik
 * Created 04.01.2009
 */
package yk.jcommon.fastgeom;

import yk.jcommon.utils.MyMath;

import java.io.Serializable;
import java.util.Objects;

import static yk.jcommon.fastgeom.Vec2i.v2i;
import static yk.jcommon.fastgeom.Vec3f.v3;

/**
 * Vec2f
 *
 * @author Yuri Kravchik Created 04.01.2009
 */
@SuppressWarnings({"SuspiciousNameCombination"})
public final class Vec2f implements Serializable {
    public float x, y;

    public static final Vec2f ZERO = new Vec2f();
    public static final Vec2f AXIS_X = new Vec2f(1, 0);
    public static final Vec2f AXIS_Y = new Vec2f(0, 1);

    //constructors
    public Vec2f() {}
    public static Vec2f fromAngle(float angle) {return new Vec2f((float)Math.cos(angle), (float) Math.sin(angle));}
    public Vec2f copy() {return new Vec2f(x, y);}
    public void copyFrom(Vec2f other) {this.x = other.x;this.y = other.y;}
    public Vec2i toVec2i() {return v2i((int)x, (int)y);}

    //setters
    public Vec3f v3WithX(float x) {return v3(x, this.x, y);}
    public Vec3f v3WithY(float y) {return v3(x, y, this.y);}
    public Vec3f v3WithZ(float z) {return v3(x, y, z);}
    public Vec2f withX(float x) {return new Vec2f(x, y);}
    public Vec2f withY(float y) {return new Vec2f(x, y);}

    //math
    public Vec2f rot90() {return new Vec2f(-y, x);}
    public Vec2f rot_90() {return new Vec2f(y, -x);}
    public float cross(Vec2f other) {return x * other.y - y * other.x;}
    public Vec2f cDiv(Vec2f by) {
        float len = 1 / by.length();
        return new Vec2f((x * by.x + y * by.y) * len, (-x * by.y + y * by.x) * len);
    }
    public Vec2f cDiv1(Vec2f unit) {return new Vec2f(x*unit.x+y*unit.y, -x*unit.y+y*unit.x);}
    public Vec2f cMul(Vec2f other) {return new Vec2f(x*other.x-y*other.y, x*other.y+y*other.x);}
    public float getAngle() {return (float) (Math.atan2(y, x));}
    public boolean isBehind(Vec2f b) {return this.scalarProduct(b) < 0;}

//2022 auto generated text
/*2022*///constructors
/*2022*/public Vec2f(float x, float y) {this.x = x;this.y = y;}
/*2022*/public Vec2f(Vec2f other) {x = other.x;y = other.y;}
/*2022*/public Vec2f(float b) {x = b;y = b;}
/*2022*/public static Vec2f v2(float x, float y) {return new Vec2f(x, y);}
/*2022*/public static Vec2f v2(float b) {return new Vec2f(b, b);}
/*2022*/public void fillFrom(Vec2f other) {x = other.x;y = other.y;}
/*2022*///operators
/*2022*/public Vec2f add(Vec2f b) {return new Vec2f(x + b.x, y + b.y);}
/*2022*/public Vec2f add(float b) {return new Vec2f(x + b, y + b);}
/*2022*/public Vec2f add(float x, float y) {return new Vec2f(this.x + x, this.y + y);}
/*2022*/public Vec2f sub(Vec2f b) {return new Vec2f(x - b.x, y - b.y);}
/*2022*/public Vec2f sub(float b) {return new Vec2f(x - b, y - b);}
/*2022*/public Vec2f sub(float x, float y) {return new Vec2f(this.x - x, this.y - y);}
/*2022*/public Vec2f mul(Vec2f b) {return new Vec2f(x * b.x, y * b.y);}
/*2022*/public Vec2f mul(float b) {return new Vec2f(x * b, y * b);}
/*2022*/public Vec2f mul(float x, float y) {return new Vec2f(this.x * x, this.y * y);}
/*2022*/public Vec2f div(Vec2f b) {return new Vec2f(x / b.x, y / b.y);}
/*2022*/public Vec2f div(float b) {return new Vec2f(x / b, y / b);}
/*2022*/public Vec2f div(float x, float y) {return new Vec2f(this.x / x, this.y / y);}
/*2022*///0 args functions
/*2022*/public Vec2f round() {return new Vec2f(Math.round(x), Math.round(y));}
/*2022*/public Vec2f floor() {return new Vec2f(MyMath.floorFast(x), MyMath.floorFast(y));}
/*2022*/public Vec2f ceil() {return new Vec2f(MyMath.ceil(x), MyMath.ceil(y));}
/*2022*/public Vec2f abs() {return new Vec2f(MyMath.abs(x), MyMath.abs(y));}
/*2022*///reducers
/*2022*/public float sum() {return x + y;}
/*2022*/public float product() {return x * y;}
/*2022*/public float ratio() {return x / y;}
/*2022*/public float min() {return MyMath.min(x, y);}
/*2022*/public float max() {return MyMath.max(x, y);}
/*2022*///distance
/*2022*/public float lengthSquared() {return x * x + y * y;}
/*2022*/public float distanceSquared(Vec2f b) {return MyMath.sqr(x - b.x) + MyMath.sqr(y - b.y);}
/*2022*/public float distanceSquared(float x, float y) {return MyMath.sqr(this.x - x) + MyMath.sqr(this.y - y);}
/*2022*/public float manhattanLength() {return Math.abs(x) + Math.abs(y);}
/*2022*/public float manhattanDistance(Vec2f b) {return MyMath.abs(x - b.x) + MyMath.abs(y - b.y);}
/*2022*/public float manhattanDistance(float x, float y) {return MyMath.abs(this.x - x) + MyMath.abs(this.y - y);}
/*2022*/public float length() {return MyMath.sqrt(x * x + y * y);}
/*2022*/public Vec2f normalized() {
/*2022*/    float m = 1f / MyMath.sqrt(x * x + y * y);
/*2022*/    return new Vec2f(x * m, y * m);
/*2022*/}
/*2022*/public Vec2f normalized(float len) {
/*2022*/    float m = len / (float)Math.sqrt(x * x + y * y);
/*2022*/    return new Vec2f(x * m, y * m);
/*2022*/}
/*2022*/public float distance(Vec2f b) {return MyMath.sqrt(MyMath.sqr(x - b.x) + MyMath.sqr(y - b.y));}
/*2022*/public float distance(float x, float y) {return MyMath.sqrt(MyMath.sqr(this.x - x) + MyMath.sqr(this.y - y));}
/*2022*///math
/*2022*/public float dot(Vec2f b) {return x * b.x + y * b.y;}
/*2022*/public float dot(float x, float y) {return this.x * x + this.y * y;}
/*2022*/public float scalarProduct(Vec2f b) {return dot(b);}
/*2022*/public Vec2f negative() {return new Vec2f(-x, -y);}
/*2022*///binary functions
/*2022*/public Vec2f min(Vec2f b) {return new Vec2f(MyMath.min(x, b.x), MyMath.min(y, b.y));}
/*2022*/public Vec2f min(float b) {return new Vec2f(MyMath.min(x, b), MyMath.min(y, b));}
/*2022*/public Vec2f min(float x, float y) {return new Vec2f(MyMath.min(this.x, x), MyMath.min(this.y, y));}
/*2022*/public Vec2f max(Vec2f b) {return new Vec2f(MyMath.max(x, b.x), MyMath.max(y, b.y));}
/*2022*/public Vec2f max(float b) {return new Vec2f(MyMath.max(x, b), MyMath.max(y, b));}
/*2022*/public Vec2f max(float x, float y) {return new Vec2f(MyMath.max(this.x, x), MyMath.max(this.y, y));}
/*2022*/public Vec2f cycle(Vec2f b) {return new Vec2f(MyMath.cycle(x, b.x), MyMath.cycle(y, b.y));}
/*2022*/public Vec2f cycle(float b) {return new Vec2f(MyMath.cycle(x, b), MyMath.cycle(y, b));}
/*2022*/public Vec2f cycle(float x, float y) {return new Vec2f(MyMath.cycle(this.x, x), MyMath.cycle(this.y, y));}
/*2022*/public Vec2f clamp(Vec2f min, Vec2f max) {return new Vec2f(Math.max(min.x, Math.min(x, max.x)), Math.max(min.y, Math.min(y, max.y)));}
/*2022*/public Vec2f clamp(float min, float max) {return new Vec2f(Math.max(min, Math.min(x, max)), Math.max(min, Math.min(y, max)));}
//2022 auto generated text

//gglsl auto generated text
public Vec2f plus(Vec2f arg1) {return Vec2f.v2((float)this.x+(float)arg1.x, (float)this.y+(float)arg1.y);}
public Vec2f plus(float arg1) {return Vec2f.v2((float)this.x+(float)arg1, (float)this.y+(float)arg1);}
public Vec2f plus(Float arg1) {return Vec2f.v2((float)this.x+(float)arg1, (float)this.y+(float)arg1);}
public Vec2f plus(Number arg1) {return Vec2f.v2((float)this.x+(float)arg1, (float)this.y+(float)arg1);}
public Vec2f minus(Vec2f arg1) {return Vec2f.v2((float)this.x-(float)arg1.x, (float)this.y-(float)arg1.y);}
public Vec2f minus(float arg1) {return Vec2f.v2((float)this.x-(float)arg1, (float)this.y-(float)arg1);}
public Vec2f minus(Float arg1) {return Vec2f.v2((float)this.x-(float)arg1, (float)this.y-(float)arg1);}
public Vec2f minus(Number arg1) {return Vec2f.v2((float)this.x-(float)arg1, (float)this.y-(float)arg1);}
public Vec2f multiply(Vec2f arg1) {return Vec2f.v2((float)this.x*(float)arg1.x, (float)this.y*(float)arg1.y);}
public Vec2f multiply(float arg1) {return Vec2f.v2((float)this.x*(float)arg1, (float)this.y*(float)arg1);}
public Vec2f multiply(Float arg1) {return Vec2f.v2((float)this.x*(float)arg1, (float)this.y*(float)arg1);}
public Vec2f multiply(Number arg1) {return Vec2f.v2((float)this.x*(float)arg1, (float)this.y*(float)arg1);}
public Vec2f div(Float arg1) {return Vec2f.v2((float)this.x/(float)arg1, (float)this.y/(float)arg1);}
public Vec2f div(Number arg1) {return Vec2f.v2((float)this.x/(float)arg1, (float)this.y/(float)arg1);}
public Vec2f radians() {return Vec2f.v2((float)(this.x/180f*Math.PI), (float)(this.y/180f*Math.PI));}
public Vec2f degrees() {return Vec2f.v2((float)(this.x/Math.PI*180), (float)(this.y/Math.PI*180));}
public Vec2f sin() {return Vec2f.v2((float)Math.sin(this.x), (float)Math.sin(this.y));}
public Vec2f cos() {return Vec2f.v2((float)Math.cos(this.x), (float)Math.cos(this.y));}
public Vec2f tan() {return Vec2f.v2((float)Math.tan(this.x), (float)Math.tan(this.y));}
public Vec2f asin() {return Vec2f.v2((float)Math.asin(this.x), (float)Math.asin(this.y));}
public Vec2f acos() {return Vec2f.v2((float)Math.acos(this.x), (float)Math.acos(this.y));}
public Vec2f atan() {return Vec2f.v2((float)Math.atan(this.x), (float)Math.atan(this.y));}
public Vec2f atan(Vec2f x) {return Vec2f.v2((float)Math.atan2(this.x, x.x), (float)Math.atan2(this.y, x.y));}
public Vec2f pow(Vec2f power) {return Vec2f.v2((float)Math.pow(this.x, power.x), (float)Math.pow(this.y, power.y));}
public Vec2f exp() {return Vec2f.v2((float)Math.exp(this.x), (float)Math.exp(this.y));}
public Vec2f log() {return Vec2f.v2((float)Math.log(this.x), (float)Math.log(this.y));}
public Vec2f sqrt() {return Vec2f.v2((float)Math.sqrt(this.x), (float)Math.sqrt(this.y));}
public Vec2f sign() {return Vec2f.v2(Math.signum(this.x), Math.signum(this.y));}
public Vec2f fract() {return Vec2f.v2(this.x - (float)Math.floor(this.x), this.y - (float)Math.floor(this.y));}
public Vec2f mod(Vec2f by) {return Vec2f.v2((float)(this.x-by.x*Math.floor(this.x/by.x)), (float)(this.y-by.y*Math.floor(this.y/by.y)));}
public Vec2f mix(Vec2f to, Vec2f progress) {return Vec2f.v2(this.x * (1 - progress.x) + to.x * progress.x, this.y * (1 - progress.y) + to.y * progress.y);}
public Vec2f mix(Vec2f to, float progress) {return Vec2f.v2(this.x * (1 - progress) + to.x * progress, this.y * (1 - progress) + to.y * progress);}
public Vec2f step(Vec2f value) {return Vec2f.v2(value.x < this.x ? 0 : 1, value.y < this.y ? 0 : 1);}
public Vec2f smoothstep(Vec2f to, Vec2f progress) {return Vec2f.v2(progress.x < this.x ? 0 : progress.x > to.x ? 1 : progress.x*progress.x*(3 - 2*progress.x), progress.y < this.y ? 0 : progress.y > to.y ? 1 : progress.y*progress.y*(3 - 2*progress.y));}
//gglsl auto generated text

    @Override
    public String toString() {
        return "x: " + x + " y: " + y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vec2f vec2f = (Vec2f) o;
        return Float.compare(x, vec2f.x) == 0 && Float.compare(y, vec2f.y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}