package es.g22.game_manager;

/**
 * DecodedMovement
 */
public class DecodedMovement {

    private String device_id;
    private String data;


    public DecodedMovement() {
    }

    public DecodedMovement(String device_id, String data) {
        this.device_id = device_id;
        this.data = data;
    }

    public String getDevice_id() {
        return this.device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getData() {
        return this.data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public DecodedMovement device_id(String device_id) {
        this.device_id = device_id;
        return this;
    }

    public DecodedMovement data(String data) {
        this.data = data;
        return this;
    }

    @Override
    public String toString() {
        return "{" +
            " device_id='" + getDevice_id() + "'" +
            ", data='" + getData() + "'" +
            "}";
    }

}