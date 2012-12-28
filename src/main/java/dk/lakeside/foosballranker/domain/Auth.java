package dk.lakeside.foosballranker.domain;

import com.google.gson.annotations.Expose;

public class Auth {
    @Expose
    private String playerId;
    @Expose
    private String password;

    public String getPlayerId() {
        return playerId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }
}
