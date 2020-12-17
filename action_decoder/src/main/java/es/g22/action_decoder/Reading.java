package es.g22.action_decoder;

public class Reading {
    private float x,y,z;

    public Reading(){}

    public Reading(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
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

    @Override
    public String toString() {
        return "Reading{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }

}
