package com.base.engine;

public class Vector3f {
    private float x;
    private float y;
    private float z;

    public Vector3f(float x, float y, float z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float length(){
        return (float)Math.sqrt(x * x + y * y + z * z);
    }

    public float dot(Vector3f right){
        return x * right.getX() + y * right.getY() + z * right.getZ();
    }

    public Vector3f normalize(){
        float length = length();

        return new Vector3f(x / length, y / length, z / length);
    }

    public Vector3f cross(Vector3f right){
        float x_ = y * right.getZ() - z * right.getY();
        float y_ = z * right.getX() - x * right.getZ();
        float z_ = x * right.getY() - y * right.getX();

        return new Vector3f(x_, y_, z_);
    }

    public Vector3f rotate(float angle, Vector3f axis){

        float sinAngle = (float)Math.sin(-angle);
        float cosAngle = (float)Math.cos(-angle);

        Vector3f res = this.cross(axis.mul(sinAngle)).add(           //Rotation on local X
                (this.mul(cosAngle)).add(                     //Rotation on local Z
                        axis.mul(this.dot(axis.mul(1 - cosAngle))))); //Rotation on local Y

        return res;
/*
        float sinHalfAngle = (float)Math.sin(Math.toRadians(angle / 2));
        float cosHalfAngle = (float)Math.cos(Math.toRadians(angle / 2));

        float rX = axis.getX() * sinHalfAngle;
        float rY = axis.getY() * sinHalfAngle;
        float rZ = axis.getZ() * sinHalfAngle;
        float rW = cosHalfAngle;

        Quaternion rotation = new Quaternion(rX, rY, rZ, rW);

        Quaternion conjugate = rotation.conjugate();

        Quaternion w = rotation.mul(this).mul(conjugate);

        x = w.getX();
        y = w.getY();
        z = w.getZ();

        //return this;

        return new Vector3f(w.getX(), w.getY(), w.getZ());*/
    }

    public Vector3f rotate(Quaternion rotation)
    {
        Quaternion conj = rotation.conjugate();

        Quaternion w = rotation.mul(this).mul(conj);

        return new Vector3f(w.getX(), w.getY(), w.getZ());
    }

    public Vector3f Abs()
    {
        return new Vector3f(Math.abs(x), Math.abs(y), Math.abs(z));
    }

    public Vector3f add(Vector3f right){
        return new Vector3f(x + right.getX(), y + right.getY(), z + right.getZ());
    }

    public Vector3f add(float right){
        return new Vector3f(x + right, y + right, z + right);
    }

    public Vector3f sub(Vector3f right){
        return new Vector3f(x - right.getX(), y - right.getY(), z - right.getZ());
    }

    public Vector3f sub(float right){
        return new Vector3f(x - right, y - right, z - right);
    }

    public Vector3f mul(Vector3f right){
        return new Vector3f(x * right.getX(), y * right.getY(), z * right.getZ());
    }

    public Vector3f mul(float right){
        return new Vector3f(x * right, y * right, z * right);
    }

    public Vector3f div(Vector3f right){
        return new Vector3f(x / right.getX(), y / right.getY(), z / right.getZ());
    }

    public Vector3f div(float right){
        return new Vector3f(x / right, y / right, z / right);
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

    public Vector2f getXY() { return new Vector2f(x, y); }
    public Vector2f getYZ() { return new Vector2f(y, z); }
    public Vector2f getZX() { return new Vector2f(z, x); }

    public Vector2f getYX() { return new Vector2f(y, x); }
    public Vector2f getZY() { return new Vector2f(z, y); }
    public Vector2f getXZ() { return new Vector2f(x, z); }

    public String toString(){
        return "(" + x + ", " + y + ", " + z + ")";
    }

}
