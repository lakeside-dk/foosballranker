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
import dk.lakeside.foosballranker.HtmlHelper;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import java.io.Serializable;

@Entity
public class Player implements Serializable {
    @Id
    @Expose
    private String id;
    @Expose
    private String name;
//    @Expose
    private String password;

    @Expose
    @Transient
    private int rating;
    @Expose
    @Transient
    private int startRating;
    @Transient
    private int turneringsRating;
    @Transient
    private int antalKampe;

    //used by objectify
    private Player() {
    }

    public Player(String id, String name, int rating) {
        //TODO verify id instead - throw error if invalid
        this.id = HtmlHelper.stripInput(id);
        this.name = HtmlHelper.stripInput(name);
        this.rating = rating;
    }

    public Player(String id, String name, String password) {
        //TODO verify id instead - throw error if invalid
        this.id = HtmlHelper.stripInput(id);
        this.name = HtmlHelper.stripInput(name);
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRating() {
        return rating;
    }

    public int getTurneringsRating() {
        return turneringsRating;
    }

    public void incrementRating(int increment) {
        rating += increment;
    }

    public void incrementTurneringsRating(int increment) {
        turneringsRating += increment;
    }

    public void resetRating() {
        rating = getStartRating();
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void resetAntalKampe() {
        antalKampe = 0;
    }

    public void resetTurneringsRating() {
        turneringsRating = 1000;
    }

    public int getAntalKampe() {
        return antalKampe;
    }

    public void incrementAntalKampe() {
        antalKampe++;
    }

    public int getStartRating() {
        // this updates the data
        if(startRating == 0) startRating = 1000;
        return startRating;
    }

    public void setStartRating(int startRating) {
        this.startRating = startRating;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;

        if (!id.equals(player.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
