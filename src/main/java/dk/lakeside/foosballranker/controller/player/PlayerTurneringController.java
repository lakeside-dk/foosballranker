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
import dk.lakeside.foosballranker.controller.Controller;
import dk.lakeside.foosballranker.controller.turnering.TurneringSingleController;
import dk.lakeside.foosballranker.domain.Player;
import dk.lakeside.foosballranker.domain.Tournament;
import dk.lakeside.foosballranker.view.HtmlView;
import dk.lakeside.foosballranker.view.JSonView;
import dk.lakeside.foosballranker.view.View;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerTurneringController implements Controller {

    public View service(Context context) throws IOException {

        if (context.hasRoot("save")) {
            return context.subContext().service(new PlayerTurneringSaveController());
        } else if (context.hasRoot("html")) {
            return showListInHtml(context);
        } else if (context.hasRoot("json")) {
            return showListInJSON(context);
        } else {
            Controller controller = new TurneringSingleController();
            return context.prepareSubContext(controller).service(controller);
        }
    }

    private View showListInHtml(Context context) throws IOException {
        Map<String,List<Tournament>> datamodel = new HashMap<String,List<Tournament>>();
        Player player = PlayerContext.getPlayer(context);
        List<Tournament> turneringer = context.getModel().getTurneringer(player);
        datamodel.put("turneringer", turneringer);
        return new HtmlView("player-turnering-list-html.ftl", datamodel);
    }

    private View showListInJSON(Context context) throws IOException {
        Player player = PlayerContext.getPlayer(context);
        List<Tournament> turneringer = context.getModel().getTurneringer(player);
        return new JSonView(turneringer);
    }

}
