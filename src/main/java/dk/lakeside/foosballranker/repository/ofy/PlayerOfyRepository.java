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
package dk.lakeside.foosballranker.repository.ofy;

import com.googlecode.objectify.*;
import dk.lakeside.foosballranker.domain.Player;
import dk.lakeside.foosballranker.repository.PlayerRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PlayerOfyRepository implements PlayerRepository {
    public Player get(String id) {
        Objectify ofy = ObjectifyService.begin();
        try {
            return ofy.get(new Key<Player>(Player.class, id));
        } catch (NotFoundException e) {
            return null;
        }
    }

    public List<Player> get(Collection<String> ids) {
        Objectify ofy = ObjectifyService.begin();
        try {
            return new ArrayList<Player>(ofy.get(Player.class, ids).values());
        } catch (NotFoundException e) {
            return null;
        }
    }

    public List<Player> findAll() {
        Objectify ofy = ObjectifyService.begin();
        List<Player> players = new ArrayList<Player>();
        Query<Player> playerQuery = ofy.query(Player.class);
        for (Player player: playerQuery) {
            players.add(player);
        }
        return players;
    }

    public void put(Player player) {
        //TODO put ofy's in context?
        Objectify ofy = ObjectifyService.begin();
        ofy.put(player);
    }
}
