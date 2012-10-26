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

@Entity
public class TournamentRelation {
    @Id
    private Long id;
    @Expose
    private String playerId;
    @Expose
    private Long turneringId;
    @Expose
    private int startRating;

    private TournamentRelation() {
    }

    public TournamentRelation(Long turneringId, String playerId, int startRating) {
        this.turneringId = turneringId;
        this.playerId = playerId;
        this.startRating = startRating;
    }

    public String getPlayerId() {
        return playerId;
    }

    public Long getTurneringId() {
        return turneringId;
    }

    public int getStartRating() {
        return startRating;
    }

    public void setStartRating(int startRating) {
        this.startRating = startRating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TournamentRelation that = (TournamentRelation) o;

        if (!playerId.equals(that.playerId)) return false;
        if (!turneringId.equals(that.turneringId)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = playerId.hashCode();
        result = 31 * result + turneringId.hashCode();
        return result;
    }

    public void setId(long id) {
        this.id = id;
    }
}
