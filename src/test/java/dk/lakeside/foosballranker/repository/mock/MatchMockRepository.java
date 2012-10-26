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
package dk.lakeside.foosballranker.repository.mock;

import dk.lakeside.foosballranker.domain.Match;
import dk.lakeside.foosballranker.repository.MatchRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MatchMockRepository implements MatchRepository {

    private Map<Long, Match> matches = new HashMap<Long,Match>();

    public Iterable<Match> findByTournament(Long tournamentId) {
        List<Match> result = new ArrayList<Match>();
        for (Match match : matches.values()) {
            if(match.getTurneringId().equals(tournamentId)){
                result.add(match);
            }
        }
        return result;
    }

    public Iterable<Match> findByPlayer(String playerId) {
        List<Match> result = new ArrayList<Match>();
        for (Match match : matches.values()) {
            if(match.getPlayerIds().contains(playerId)){
                result.add(match);
            }
        }
        return result;
    }

    public void put(Match match) {
        long id = (long) matches.size();
        match.setId(id);
        matches.put(id, match);
    }

    public void delete(long id) {
        matches.remove(id);
    }
}
