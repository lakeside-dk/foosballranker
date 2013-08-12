package dk.lakeside.foosballranker.domain;

import com.google.gson.annotations.Expose;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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

    static char[] HEX_CHARS = "0123456789ABCDEF".toCharArray();

    public static String getPasswordHash(String s) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(s.getBytes("UTF-8"));
            byte[] hash = digest.digest();
            StringBuilder sb = new StringBuilder(hash.length * 2);
            for (byte b : hash) {
                sb.append(HEX_CHARS[(b & 0xF0) >> 4]);
                sb.append(HEX_CHARS[b & 0x0F]);
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean playerPasswordIsValid(Player player) {
        return !(getPassword() == null || player == null)
                && (player.getPassword().equals(getPassword()) || player.getPassword().equals(getPasswordHash(password)));
    }
}
