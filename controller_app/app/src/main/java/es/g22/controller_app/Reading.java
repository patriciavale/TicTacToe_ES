package es.g22.controller_app;

import org.json.JSONException;
import org.json.JSONObject;

public class Reading {
    private float x,y,z;

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

    public JSONObject toJSON(){

        JSONObject jsonObject= new JSONObject();
        try {
            jsonObject.put("x", getX());
            jsonObject.put("y", getY());
            jsonObject.put("z", getZ());
            return jsonObject;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

    }
}
