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

import dk.lakeside.foosballranker.repository.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ModelTest {

    Model model;

    @Mock
    PlayerRepository playerRepository;
    @Mock
    PlayerRelationRepository playerRelationRepository;
    @Mock
    TournamentRepository tournamentRepository;
    @Mock
    TournamentRelationsRepository tournamentRelationsRepository;
    @Mock
    MatchRepository matchRepository;

    @Before
    public void setup() {
        model = new Model();
        model.playerRelationRepository = playerRelationRepository;
        model.playerRepository = playerRepository;
        model.tournamentRepository = tournamentRepository;
        model.tournamentRelationsRepository = tournamentRelationsRepository;
        model.matchRepository = matchRepository;
    }

    @Test
    public void whenAddKampWithNoRelationsThenRelationsShouldBeCreated() {
        ArrayList<Player> players = new ArrayList<Player>();
        players.add(new Player("sbv", "Simon", 1000));
        players.add(new Player("ksr", "Kent", 1000));
        when(playerRepository.get(anyCollection())).thenReturn(players);
        when(playerRelationRepository.findByPlayer(anyString())).thenReturn(new ArrayList<PlayerRelation>());
        Tournament tournament = new Tournament(Tournament.RANKING, "tour1");
        tournament.setId(1L);
        when(tournamentRepository.get(anyLong())).thenReturn(tournament);
        when(tournamentRelationsRepository.findByTournament(anyLong())).thenReturn(new ArrayList<TournamentRelation>());

        Match match = new Match("sbv", 1L, 0, 10, "sbv", "ksr");
        model.addMatch(match);

        Mockito.verify(tournamentRelationsRepository, times(1)).put(new TournamentRelation(1L, "sbv", 1000));
        Mockito.verify(tournamentRelationsRepository, times(1)).put(new TournamentRelation(1L, "ksr", 1000));
        Mockito.verify(playerRelationRepository, times(1)).put(new PlayerRelation("sbv", "ksr"));
        Mockito.verify(playerRelationRepository, times(1)).put(new PlayerRelation("ksr", "sbv"));
        Mockito.verify(matchRepository, times(1)).put(match);
    }
}
