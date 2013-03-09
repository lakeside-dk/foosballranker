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

import dk.lakeside.foosballranker.JSonHelper;
import dk.lakeside.foosballranker.Pair;
import dk.lakeside.foosballranker.repository.mock.*;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertEquals;

public class MoreModelTest {

    Model model;

    @Before
    public void setup() {
        DataBuilder.clearInstance();
        model = new Model();
        model.playerRelationRepository = new PlayerRelationMockRepository();
        model.playerRepository = new PlayerMockRepository();
        model.tournamentRepository = new TournamentMockRepository();
        model.tournamentRelationsRepository = new TournamentRelationsMockRepository();
        model.matchRepository = new MatchMockRepository();
    }

    @Test
    public void whenAddTwoMatchesOnSamePlayersThenChartOnlyShowCompetitorOnce() {
        DataBuilder.instance(model)
                .addPlayer()
                .addRankingTournament()
                .addPlayer()
                .addSingleMatch(10, 0)
                .addSingleMatch(10, 0);
        Pair<List<Player>,List<List<Integer>>> chartData = model.generatePlayerRatingChartData(model.getPlayer("player0"), true);
        assertEquals(2, chartData.getFirst().size());
    }

    @Test
    public void whenGenerateChartDataThenVerifyJsonFormat() {
        DataBuilder.instance(model)
                .addPlayer()
                .addRankingTournament()
                .addPlayer()
                .addSingleMatch(10, 0)
                .addSingleMatch(10, 0);
        List<List<Object>> chartData = model.generatePlayerRatingChartData2(model.getPlayer("player0"));
        String data = JSonHelper.toJSon(chartData);
        assertEquals("[\n" +
                "  [\n" +
                "    \"Match\",\n" +
                "    \"player0\",\n" +
                "    \"player1\"\n" +
                "  ],\n" +
                "  [\n" +
                "    0,\n" +
                "    1000,\n" +
                "    1000\n" +
                "  ],\n" +
                "  [\n" +
                "    1,\n" +
                "    1025,\n" +
                "    975\n" +
                "  ],\n" +
                "  [\n" +
                "    2,\n" +
                "    1046,\n" +
                "    954\n" +
                "  ]\n" +
                "]", data);
    }

    @Test
    public void whenCreateTournamentThenCheckCorrectStartRating() {
        DataBuilder.instance(model)
                .addPlayer()
                .addRankingTournament()
                .addPlayer()
                .addSingleMatch(10, 0)      // p1 1025, p2 975
                .addPerformanceTournament()
                .addSingleMatch(1L, 10, 0);     // p1 1037, p2 956
        assertEquals(1025, model.tournamentRelationsRepository.findByTournamentAndPlayer(1L, "player0").getStartRating());
    }

    @Test
    public void whenCreatePerformanceTournamentThenCheckCorrectStartRating() {
        DataBuilder.instance(model)
                .addPlayer()
                .addPlayer()
                .addPerformanceTournament()
                .addSingleMatch(0L, 10, 0);
        assertEquals(1000, model.tournamentRelationsRepository.findByTournamentAndPlayer(0L, "player0").getStartRating());
    }

    @Test
    public void whenShowTournamentChartWithNoMatchesThenShowEmptyChartWithOnlyThePlayerCreatingTheTournament() {
        DataBuilder.instance(model)
                .addPlayer()
                .addRankingTournament()
                .addPlayer();
        Pair<List<Player>, List<List<Integer>>> chartData = model.generateTurneringRatingChartData(model.getTurnering(0L));
        // one player
        assertEquals(1, chartData.getFirst().size());
        // zero matches - only startrating
        assertEquals(1, chartData.getSecond().get(0).size());
    }

    @Test
    public void whenShowPlayerChartWithNoMatchesThenShowEmptyChartWithOnlyThePlayersStartrating() {
        DataBuilder.instance(model)
                .addPlayer();
        Pair<List<Player>, List<List<Integer>>> chartData = model.generatePlayerRatingChartData(model.getPlayer("player0"), true);
        // one player
        assertEquals(1, chartData.getFirst().size());
        // zero matches - only startrating
        assertEquals(1, chartData.getSecond().get(0).size());
    }

    @Test
    public void whenShowOpponentRankingThenDontFail() {
        DataBuilder.instance(model)
                .addPlayer()
                .addPlayer()
                .addRankingTournament()
                .addSingleMatch(10, 0)
                .addSingleMatch(10, 0)
                .addSingleMatch(10, 0)
                .addSingleMatch(10, 0)
                .addSingleMatch(10, 0);     // p1 1037, p2 956
        List<Pair<String, String>> chartData = model.getPlayerAndCompetitorsWithRating(model.getPlayer("player0"));
        assertEquals(2, chartData.size());
        assertEquals("Player0", chartData.get(0).getFirst());
        assertEquals("1046", chartData.get(0).getSecond());
        assertEquals("Player1", chartData.get(1).getFirst());
        assertEquals("954", chartData.get(1).getSecond());
        // one player
//        assertEquals(1, chartData.getFirst().size());
        // zero matches - only startrating
//        assertEquals(1, chartData.getSecond().get(0).size());
    }
}
