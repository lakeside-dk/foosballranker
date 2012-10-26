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

import dk.lakeside.foosballranker.Pair;
import dk.lakeside.foosballranker.controller.Context;
import dk.lakeside.foosballranker.controller.InvalidPathException;
import dk.lakeside.foosballranker.controller.SecureController;
import dk.lakeside.foosballranker.domain.Player;
import dk.lakeside.foosballranker.view.HtmlView;
import dk.lakeside.foosballranker.view.View;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerModstanderController implements SecureController {

    public View service(final Context context) throws IOException {
        if (context.hasRoot("add")) {
            Player player = PlayerContext.getPlayer(context);
            return context.subContext().service(new PlayerModstanderAddController(player));
        } else if (context.hasRoot("html")) {
            return showModstander(context);
        } else if (context.hasRoot("bigsvgchart")) {
            return showChart(context, true);
        } else if (context.hasRoot("svgchart")) {
            return showChart(context, false);
        }
        throw new InvalidPathException(context);
    }

    protected View showChart(Context context, boolean fullscreen) {
        Player player = PlayerContext.getPlayer(context);
        final Pair<List<Player>,List<List<Integer>>> playerRatingsData = context.getModel().generatePlayerRatingChartData(player);
        // Add the values in the datamodel
        Map<String,Object> datamodel = new HashMap<String,Object>();
        StringBuilder data = new StringBuilder();
        data.append("data.addColumn('string', 'x');\n");

        for (Player chartedPlayer : playerRatingsData.getFirst()) {
            data.append("data.addColumn('number', '" + chartedPlayer.getId() + "');\n");
        }
        int i = 0;
        for (List<Integer> ratingsAfterKamp : playerRatingsData.getSecond()) {
            data.append("data.addRow([\"" + i++ + "\"");
            for (Integer integer : ratingsAfterKamp) {
                data.append(", ").append(integer);
            }
            data.append("]);\n");
        }

        datamodel.put("titel", "Players ranking");
        datamodel.put("data", data.toString());
        datamodel.put("baseline", 1000);
        if(fullscreen) {
            return new HtmlView("bigsvgchart.ftl", datamodel);
        } else {
            return new HtmlView("svgchart.ftl", datamodel);
        }
    }

    private View showModstander(Context context) {
        Player player = PlayerContext.getPlayer(context);
        List<Pair<String,String>> playerAndCompetitorsWithRating = context.getModel().getPlayerAndCompetitorsWithRating(player);
        Map<String,Object> datamodel = new HashMap<String,Object>();
        datamodel.put("modstandere", playerAndCompetitorsWithRating);
        return new HtmlView("player-modstander-html.ftl", datamodel);
    }
}
