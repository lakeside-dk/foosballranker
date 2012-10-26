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

import dk.lakeside.foosballranker.Pair;
import dk.lakeside.foosballranker.controller.Context;
import dk.lakeside.foosballranker.controller.Controller;
import dk.lakeside.foosballranker.controller.InvalidPathException;
import dk.lakeside.foosballranker.domain.Player;
import dk.lakeside.foosballranker.domain.PlayerRatingSnapshot;
import dk.lakeside.foosballranker.domain.Tournament;
import dk.lakeside.foosballranker.view.HtmlView;
import dk.lakeside.foosballranker.view.View;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class TurneringAbstractRankingController implements Controller {
    protected Tournament tournament;

    public TurneringAbstractRankingController(Tournament tournament) {
        this.tournament = tournament;
    }

    public View service(final Context context) throws IOException {

        if (context.hasRoot("html")) {
            return showHtml(context);
        } else if (context.hasRoot("bigsvgchart")) {
            return showChart(context, true);
        } else if (context.hasRoot("svgchart")) {
            return showChart(context, false);

        } else {
            throw new InvalidPathException(context);
        }
    }

    protected View showHtml(Context context) {
        Collection<PlayerRatingSnapshot> players = getSortedPlayers(context);
        Map<String,Object> datamodel = new HashMap<String,Object>();
        datamodel.put("turnering", tournament);
        datamodel.put("players", players);
        return new HtmlView("turnering-ranking-html.ftl", datamodel);
    }


    protected View showChart(final Context context, boolean fullscreen) {
        final Pair<List<Player>,List<List<Integer>>> playerRatingsData = createChartData(context);
        StringBuilder data = new StringBuilder();
        data.append("data.addColumn('string', 'x');\n");

        for (Player player : playerRatingsData.getFirst()) {
            data.append("data.addColumn('number', '" + player.getId() + "');\n");
        }
        int i = 0;
        for (List<Integer> ratingsAfterKamp : playerRatingsData.getSecond()) {
            data.append("data.addRow([\"" + i++ + "\"");
            for (Integer integer : ratingsAfterKamp) {
                data.append(", ").append(integer);
            }
            data.append("]);\n");
        }

        String dataset = data.toString();

        // Add the values in the datamodel
        Map<String,Object> datamodel = new HashMap<String,Object>();
        datamodel.put("titel", "Tournament "+ tournament.getName());
        datamodel.put("data", dataset);
        datamodel.put("baseline", getBaseline());
        if(fullscreen) {
            return new HtmlView("bigsvgchart.ftl", datamodel);
        } else {
            return new HtmlView("svgchart.ftl", datamodel);
        }
    }

    protected abstract int getBaseline();

    protected abstract Pair<List<Player>, List<List<Integer>>> createChartData(Context context);

    protected abstract Collection<PlayerRatingSnapshot> getSortedPlayers( Context context );
}