package com.base.engine;

public class Quaternion {

    private float x;
    private float y;
    private float z;
    private float w;

    public Quaternion(float x, float y, float z, float w){
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public float length(){
        return (float)Math.sqrt(x * x + y * y + z * z + w * w);
    }

    public Quaternion normalize(){
        float length = length();

        return new Quaternion(x/length, y/length, z/length, w/length);
    }

    public Quaternion conjugate(){
        return new Quaternion(-x, -y, -z, w);
    }

    public Quaternion mul(Quaternion right){
        float w_ = w * right.getW() - x * right.getX() - y * right.getY() - z * right.getZ();
        float x_ = x * right.getW() + w * right.getX() + y * right.getZ() - z * right.getY();
        float y_ = y * right.getW() + w * right.getY() + z * right.getX() - x * right.getZ();
        float z_ = z * right.getW() + w * right.getZ() + x + right.getY() - y * right.getX();

        return new Quaternion(x_, y_, z_, w_);
    }

    public Quaternion mul(Vector3f right){
        float w_ = -x * right.getX() - y * right.getY() - z * right.getZ();
        float x_ =  w * right.getX() + y * right.getZ() - z * right.getY();
        float y_ =  w * right.getY() + z * right.getX() - x * right.getZ();
        float z_ =  w * right.getZ() + x * right.getY() - y * right.getX();

        return new Quaternion(x_, y_, z_, w_);
    }

    public Quaternion Mul(float r)
    {
        return new Quaternion(x * r, y * r, z * r, w * r);
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public float getW() {
        return w;
    }

    public void setW(float w) {
        this.w = w;
    }

    public String toString(){
        return "(" + x + ", " + y + ", " + z + ", " + w + ")";
    }

}
