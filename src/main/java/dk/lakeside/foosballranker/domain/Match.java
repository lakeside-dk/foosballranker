/*
 * The MIT License
 *
 * Original work sponsored and donated by Lakeside A/S (http://www.lakeside.dk)
 *
 * Copyright (c) to all contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package dk.lakeside.foosballranker.domain;

import com.google.gson.annotations.Expose;
import com.googlecode.objectify.annotation.Indexed;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Match {
    @Id
    @Expose
    Long id;
    @Expose
    Date time = new Date();
    @Expose
    int score1;
    @Expose
    int score2;
    //TODO fix createdBy - should always be set
    @Expose
    String createdById = "";
    @Expose
    Long turneringId;
    @Expose
    @Indexed
    List<String> playerIds;

    //used by objectify
    Match() {
    }

    private Match(int score1, int score2, String player1Id, String player2Id) {
        this.score1 = score1;
        this.score2 = score2;
        playerIds = new ArrayList<String>(2);
        playerIds.add(player1Id);
        playerIds.add(player2Id);
    }

    public Match(String createdById, Long turneringId, int score1, int score2, String player1Id, String player2Id) {
        this(score1, score2, player1Id, player2Id);
        this.createdById = createdById;
        this.turneringId = turneringId;
    }

    public void setTurneringId(Long turneringId) {
        this.turneringId = turneringId;
    }

    public void ensureId(long suggestion) {
        if (id == null) id = suggestion;
    }

    public Date getTime() {
        return time;
    }

    public Long getId() {
        return id;
    }

    public int getScore1() {
        return score1;
    }

    public int getScore2() {
        return score2;
    }

    public String getCreatedById() {
        return createdById;
    }

    public Long getTurneringId() {
        return turneringId;
    }

    public boolean hasPlayer(String playerId) {
        return playerIds.contains(playerId);
    }

    public boolean isInTurnering(String id) {
        return id.equals(turneringId);
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Match match = (Match) o;

        if (score1 != match.score1) return false;
        if (score2 != match.score2) return false;
        if (!createdById.equals(match.createdById)) return false;
        if (!playerIds.equals(match.playerIds)) return false;
        if (!time.equals(match.time)) return false;
        if (turneringId != null ? !turneringId.equals(match.turneringId) : match.turneringId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = time.hashCode();
        result = 31 * result + score1;
        result = 31 * result + score2;
        result = 31 * result + createdById.hashCode();
        result = 31 * result + (turneringId != null ? turneringId.hashCode() : 0);
        result = 31 * result + playerIds.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Match{" +
                "id=" + id +
                ", time=" + time +
                ", score1=" + score1 +
                ", score2=" + score2 +
                ", createdById='" + createdById + '\'' +
                ", turneringId=" + turneringId +
                ", playerIds=" + playerIds +
                '}';
    }

    public boolean isSingle() {
        return playerIds.size() == 2;
    }

    public String getPlayer1Id() {
        return isSingle() ? playerIds.get(0) : null;
    }

    public String getPlayer2Id() {
        return isSingle() ? playerIds.get(1) : null;
    }

    public String getDefender1Id() {
        return isDouble() ? playerIds.get(0) : null;
    }

    public String getAttacker1Id() {
        return isDouble() ? playerIds.get(1) : null;
    }

    public String getDefender2Id() {
        return isDouble() ? playerIds.get(2) : null;
    }

    public String getAttacker2Id() {
        return isDouble() ? playerIds.get(3) : null;
    }

    public boolean isDouble() {
        return !isSingle();
    }

    public List<String> getPlayerIds() {
        return playerIds;
    }
}
