package es.g22.action_decoder;

/**
 * MovementData
 */
public class MovementData {

    private String device_id;
    private Reading[] data;


    public MovementData() {
    }

    public MovementData(String device_id, Reading[] data) {
        this.device_id = device_id;
        this.data = data;
    }

    public String getDevice_id() {
        return this.device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public Reading[] getData() {
        return this.data;
    }

    public void setData(Reading[] data) {
        this.data = data;
    }

    public MovementData device_id(String device_id) {
        this.device_id = device_id;
        return this;
    }

    public MovementData data(Reading[] data) {
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