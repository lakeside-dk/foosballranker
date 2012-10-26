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

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

/**
 * This relation helps us fetching a players competitors - if you add a competitor or playes against a player - youll get this relation with him.
 */
@Entity
public class PlayerRelation {
    @Id
    private Long id;

    @Expose
    @Transient
    private String player1Id;
    @Expose
    @Transient
    private String player2Id;
    @Expose
    private String playerId;
    @Expose
    private String competitorId;

    private PlayerRelation() {
    }

    public PlayerRelation(String playerId, String competitorId) {
        this.playerId = playerId;
        this.competitorId = competitorId;
    }

    public String getPlayerId() {
        return playerId;
    }

    public String getCompetitorId() {
        return competitorId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlayerRelation that = (PlayerRelation) o;

        if (!playerId.equals(that.playerId)) return false;
        if (!competitorId.equals(that.competitorId)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = playerId.hashCode();
        result = 31 * result + competitorId.hashCode();
        return result;
    }

    public String getPlayer1Id() {
        return player1Id;
    }

    public void setPlayer1Id(String player1Id) {
        this.player1Id = player1Id;
    }

    public String getPlayer2Id() {
        return player2Id;
    }

    public void setPlayer2Id(String player2Id) {
        this.player2Id = player2Id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
