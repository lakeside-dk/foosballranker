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

import dk.lakeside.foosballranker.domain.TournamentRelation;
import dk.lakeside.foosballranker.repository.TournamentRelationsRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TournamentRelationsMockRepository implements TournamentRelationsRepository {

    private Map<Long, TournamentRelation> tournamentRelations = new HashMap<Long,TournamentRelation>();

    public Iterable<TournamentRelation> findByTournament(Long tournamentId) {
        List<TournamentRelation> result = new ArrayList<TournamentRelation>();
        for (TournamentRelation tournamentRelation : tournamentRelations.values()) {
            if(tournamentRelation.getTurneringId().equals(tournamentId)){
                result.add(tournamentRelation);
            }
        }
        return result;
    }

    public Iterable<TournamentRelation> findByPlayer(String playerId) {
        List<TournamentRelation> result = new ArrayList<TournamentRelation>();
        for (TournamentRelation tournamentRelation : tournamentRelations.values()) {
            if(tournamentRelation.getPlayerId().equals(playerId)){
                result.add(tournamentRelation);
            }
        }
        return result;
    }

    public TournamentRelation findByTournamentAndPlayer(Long tournamentId, String playerId) {
        for (TournamentRelation tournamentRelation : findByTournament(tournamentId)) {
            if(tournamentRelation.getPlayerId().equals(playerId)){
                return tournamentRelation;
            }
        }
        return null;
    }

    public void put(TournamentRelation tournamentRelation) {
        long id = (long) tournamentRelations.size();
        tournamentRelation.setId(id);
        tournamentRelations.put(id, tournamentRelation);
    }

}
