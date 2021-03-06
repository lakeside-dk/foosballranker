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
import dk.lakeside.foosballranker.servlet.RequestSource;
import dk.lakeside.foosballranker.controller.SecureController;
import dk.lakeside.foosballranker.domain.Player;
import dk.lakeside.foosballranker.view.JSonView;
import dk.lakeside.foosballranker.view.View;

import java.io.IOException;

public class PlayerModstanderAddController implements SecureController {
    private Player player;

    public PlayerModstanderAddController(Player player) {
        this.player = player;
    }

    public View service(final Context context) throws IOException {
        RequestSource params = context.getParameters();
        String id = params.getParameter("id");

        if(id == null || "".equals(id)) {
            throw new RuntimeException("player with id '" + id + "' dont exist");
        }

        Player modstander = context.getModel().getPlayer(id);

        if(modstander == null) {
            throw new RuntimeException("player with id '" + id + "' dont exist");
        }

        if(context.getModel().playerHasPlayerRelation(player.getId(), id)) {
            throw new RuntimeException("players allready linked");
        }

        context.getModel().putPlayer(modstander);
        //TODO avoid add relation twice
        context.getModel().addPlayerRelation(player, modstander);
        context.getModel().addPlayerRelation(modstander, player);
        System.out.println("modstander added");

        return new JSonView("");
    }
}
