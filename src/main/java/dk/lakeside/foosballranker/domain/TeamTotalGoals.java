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

public class TeamTotalGoals extends Team implements Comparable<TeamTotalGoals> {

    @Expose
    private Integer goals = 0;
    @Expose
    private Integer totalGoals = 0;
    @Expose
    private Integer antalKampe = 0;

    public TeamTotalGoals(Team team) {
        super(team.getPlayer1(), team.getPlayer2());
    }

    public void addGoals(Integer goals) {
        this.goals += goals;
    }

    public void addKamp() {
        antalKampe++;
    }

    public Integer getTotalGoals() {
        return totalGoals;
    }

    public Integer getGoals() {
        return goals;
    }

    public Integer getAntalKampe() {
        return antalKampe;
    }

    public Double getGoalPercentage() {
        return (goals+0.0) / totalGoals * 100.0;
    }

    public int compareTo(TeamTotalGoals teamTotalGoals) {
        // most first
        return teamTotalGoals.getGoalPercentage().compareTo(getGoalPercentage());
    }

    public void addTotalGoals(Integer totalGoals) {
        this.totalGoals += totalGoals;
    }
}
