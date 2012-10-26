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
package dk.lakeside.foosballranker.controller.turnering;

import dk.lakeside.foosballranker.controller.Context;
import dk.lakeside.foosballranker.controller.Controller;
import dk.lakeside.foosballranker.controller.InvalidPathException;
import dk.lakeside.foosballranker.controller.player.PlayerContext;
import dk.lakeside.foosballranker.controller.player.PlayerKampAddController;
import dk.lakeside.foosballranker.controller.player.PlayerKampDataController;
import dk.lakeside.foosballranker.domain.Match;
import dk.lakeside.foosballranker.domain.ModelHelper;
import dk.lakeside.foosballranker.domain.Player;
import dk.lakeside.foosballranker.domain.Tournament;
import dk.lakeside.foosballranker.view.HtmlView;
import dk.lakeside.foosballranker.view.JSonView;
import dk.lakeside.foosballranker.view.View;

import java.io.IOException;
import java.util.*;

public class TurneringKampController implements Controller {
    public View service(final Context context) throws IOException {
        Tournament tournament = TurneringContext.getTurnering(context);
        if (context.hasRoot("list")) {
            List<Match> kampe = context.getModel().getKampe(tournament);
            ModelHelper.sortKampeNewestFirst(kampe);
            if (context.hasRoot("list/html")) {
                return showKampeHtml(kampe);
            } else {
                return new JSonView(kampe);
            }
        } else if (context.hasRoot("add")) {
            return context.subContext().service(new PlayerKampAddController());
        } else if (context.hasRoot("data")) {
            return context.subContext().service(new PlayerKampDataController());
        } else if (context.hasRoot("html")) {
            return showKampInput(context);
        } else if (context.hasRoot("delete")) {
            return context.subContext().service(new TurneringKampDeleteController(tournament));
        } else {
            throw new InvalidPathException(context);
        }
    }

    private View showKampInput(Context context) {
        Player player = PlayerContext.getPlayer(context);
        Map<String,Object> datamodel = new HashMap<String,Object>();
        List<Tournament> turneringer = context.getModel().getAktiveTurneringer(player);

        Set<Player> playersSet = new HashSet<Player>(context.getModel().getPlayerAndCompetitors(player));
        for (Tournament tournament : turneringer) {
            playersSet.addAll(context.getModel().getPlayers(tournament));
        }
        List<Player> players = new ArrayList<Player>(playersSet);
        ModelHelper.sortPlayersByName(players);

        datamodel.put("turneringer", turneringer);
        datamodel.put("players", players);
        return new HtmlView("player-kamp-html.ftl", datamodel);
    }

    private View showKampeHtml(List<Match> kampe) {
        Map<String,List<Match>> datamodel = new HashMap<String, List<Match>>();
        datamodel.put("kampe", kampe);
        return new HtmlView("turnering-kamp-list-html.ftl", datamodel);
    }
}
