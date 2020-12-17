package es.g22.game_manager;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 * GameAction
 */
@Entity
public class GameAction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private Long game_id;
    private String device_id;
    private String action;
    
    public GameAction() {
    }

    public GameAction(Long game_id, String device_id, String action) {
        this.game_id = game_id;
        this.device_id = device_id;
        this.action = action;
    }

    public Long getGame_id() {
        return this.game_id;
    }

    public void setGame_id(Long game_id) {
        this.game_id = game_id;
    }

    public String getDevice_id() {
        return this.device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getAction() {
        return this.action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public GameAction game_id(Long game_id) {
        this.game_id = game_id;
        return this;
    }

    public GameAction device_id(String device_id) {
        this.device_id = device_id;
        return this;
    }

    public GameAction action(String action) {
        this.action = action;
        return this;
    }
    @Override
    public String toString() {
        return "{" +
            " game_id='" + getGame_id() + "'" +
            ", device_id='" + getDevice_id() + "'" +
            ", action='" + getAction() + "'" +
            "}";
    }

}
