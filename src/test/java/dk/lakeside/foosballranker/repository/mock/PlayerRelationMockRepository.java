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

import dk.lakeside.foosballranker.domain.PlayerRelation;
import dk.lakeside.foosballranker.repository.PlayerRelationRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerRelationMockRepository implements PlayerRelationRepository {

    private Map<Long, PlayerRelation> playerRelations = new HashMap<Long,PlayerRelation>();

    public Iterable<PlayerRelation> findByPlayer(String playerId) {
        List<PlayerRelation> result = new ArrayList<PlayerRelation>();
        for (PlayerRelation playerRelation : playerRelations.values()) {
            if(playerRelation.getPlayerId().equals(playerId)){
                result.add(playerRelation);
            }
        }
        return result;
    }

    public void put(PlayerRelation playerRelation) {
        long id = (long) playerRelations.size();
        playerRelation.setId(id);
        playerRelations.put(id, playerRelation);
    }
}
