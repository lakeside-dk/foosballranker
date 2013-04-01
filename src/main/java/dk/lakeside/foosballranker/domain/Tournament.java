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
import java.io.Serializable;
import java.util.Date;

@Entity
public class Tournament implements Serializable {

    public static final String RANKING = "ranking";
    public static final String PERFORMANCE = "performance";

    @Id
    @Expose
    private Long id;
    @Expose
    private String name;
    @Expose
    private Date startDate;
    @Expose
    private Date endDate;

    //used by objectify
    private Tournament() {
    }

    public String getType() {
        return type;
    }

    //TODO enum?
    @Expose
    private String type;

    public Tournament(String type, String name, Date startDate, Date endDate) {
        this(type, name);
        this.startDate = startDate;
        this.endDate = endDate;
    }
    
    public Tournament(String type, String name) {
        if(name == null || "".equals(name) || "undefined".equals(name) || !name.equals(HtmlHelper.stripInput(name))) {
            throw new RuntimeException("Invalid name.");
        }
        if(!RANKING.equals(type) && !PERFORMANCE.equals(type)) {
            throw new RuntimeException("Invalid type.");
        }

        this.name = HtmlHelper.stripInput(name);
        this.startDate = new Date();
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void afslut() {
        endDate = new Date();
    }

    public void genaabn() {
        endDate = null;
    }

    public Date getEndDate() {
        return endDate;
    }

    public boolean isTypeRanking() {
        return type.equals(RANKING);
    }

    public boolean isTypePerformance() {
        return type.equals(PERFORMANCE);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tournament tournament = (Tournament) o;

        if (!id.equals(tournament.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
