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
import dk.lakeside.foosballranker.domain.Player;
import dk.lakeside.foosballranker.repository.PlayerRepository;

import java.util.*;

public class PlayerMockRepository implements PlayerRepository {

    private Map<String, Player> players = new HashMap<String,Player>();

    public Player get(String id) {
        for (Player player : players.values()) {
            if(player.getId().equals(id)){
                return player;
            }
        }
        return null;
    }

    public List<Player> get(Collection<String> ids) {
        List<Player> result = new ArrayList<Player>();
        for (String id : ids) {
            Player player = get(id);
            if(player != null) {
                result.add(player);
            }
        }
        return result;
    }

    public List<Player> findAll() {
        return new ArrayList<Player>(players.values());
    }

    public void put(Player player) {
        players.put(player.getId(), player);
    }

    @Override
    public Iterable<Player> findByEmail(String email) {
        for (Player player : players.values()) {
            if(player.getEmail().equals(email)) {
                List<Player> result = new ArrayList<Player>();
                result.add(player);
                return result;
            }
        }
        return null;
    }
}
