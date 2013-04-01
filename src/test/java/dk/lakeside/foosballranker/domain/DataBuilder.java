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

public class DataBuilder {

    private static DataBuilder instance;

    private Model model;
    private int playerIndex;
    private int tournamentIndex;

    private DataBuilder(Model model) {
        this.model = model;
    }

    public static DataBuilder instance(Model model) {
        if(instance == null) {
            instance = new DataBuilder(model);
        }
        return instance;
    }

    public DataBuilder addRankingTournament() {
        model.addTurnering(new Tournament(Tournament.RANKING, "tour"+tournamentIndex++), model.getPlayer("player0"));
        return instance;
    }

    public DataBuilder addPerformanceTournament() {
        model.addTurnering(new Tournament(Tournament.PERFORMANCE, "tour"+tournamentIndex++), model.getPlayer("player0"));
        return instance;
    }

    public DataBuilder addPlayer() {
        model.addPlayer(new Player("player"+ playerIndex, "Player"+playerIndex++, "pwd"));
        return instance;
    }

    public DataBuilder addSingleMatch(int score1, int score2) {
        tinySleep();

        model.addMatch(new Match("player0", 0L, score1, score2, "player0", "player1"));
        return instance;
    }

    public DataBuilder addSingleMatch(long tournamentId, int score1, int score2) {
        tinySleep();
        model.addMatch(new Match("player0", tournamentId, score1, score2, "player0", "player1"));
        return instance;
    }

    public DataBuilder addDoubleMatch(int score1, int score2) {
        tinySleep();

        model.addMatch(new Match("player0", 0L, score1, score2, "player0", "player1", "player2", "player3"));
        return instance;
    }

    // this hack avoids unittests adding multiple matches with same timestamp
    private void tinySleep() {
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void clearInstance() {
        instance = null;
    }
}
