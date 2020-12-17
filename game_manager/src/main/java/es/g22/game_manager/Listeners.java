package es.g22.game_manager;

import java.util.HashMap;

import org.springframework.stereotype.Component;

/**
 * Listeners
 */
@Component
public class Listeners {

    private HashMap<String,Long> relations;

    public Listeners() {
        this.relations = new HashMap<String,Long>();
    }

    public Listeners(HashMap<String,Long> relations) {
        this.relations = relations;
    }

    public HashMap<String,Long> getRelations() {
        return this.relations;
    }

    public void setRelations(HashMap<String,Long> relations) {
        this.relations = relations;
    }

    public Listeners relations(HashMap<String,Long> relations) {
        this.relations = relations;
        return this;
    }

    public boolean addDevice(String device_id, Long game){
        relations.put(device_id, game);
        return true;
    }

    public boolean removeDevice(String device_id){
        relations.remove(device_id);
        return true;
    }


    @Override
    public String toString() {
        return "{" +
            " relations='" + getRelations() + "'" +
            "}";
    }

}