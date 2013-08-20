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
package dk.lakeside.foosballranker.controller.player;

import dk.lakeside.foosballranker.controller.Context;
import dk.lakeside.foosballranker.controller.SecureController;
import dk.lakeside.foosballranker.controller.turnering.TurneringContext;
import dk.lakeside.foosballranker.domain.ModelHelper;
import dk.lakeside.foosballranker.domain.Player;
import dk.lakeside.foosballranker.domain.Tournament;
import dk.lakeside.foosballranker.view.JSonView;
import dk.lakeside.foosballranker.view.View;

import java.io.IOException;
import java.util.*;

public class PlayerKampDataController implements SecureController {

    public View service(Context context) throws IOException {
        Player player = PlayerContext.getPlayer(context);
        Tournament tournament = TurneringContext.getTurnering(context);

        Map<String,Object> datamodel = new HashMap<String,Object>();

        Set<Player> playersSet = new HashSet<Player>(context.getModel().getPlayerAndCompetitors(player));

        List<Player> players = new ArrayList<Player>(playersSet);
        ModelHelper.sortPlayersByName(players);

        //TODO split up in players with relation in tournament, players in tournament with no relation, players with relation not in tournament
        datamodel.put("players", players);
        datamodel.put("player", context.currentUser());

        return new JSonView(datamodel);
    }
}
