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
package dk.lakeside.foosballranker.controller;

import dk.lakeside.foosballranker.controller.player.PlayerModstanderController;
import dk.lakeside.foosballranker.controller.player.PlayerSingleController;
import dk.lakeside.foosballranker.domain.Model;
import dk.lakeside.foosballranker.domain.Player;
import dk.lakeside.foosballranker.domain.PlayerWithRating;
import dk.lakeside.foosballranker.servlet.RequestSource;
import dk.lakeside.foosballranker.servlet.SessionSource;
import dk.lakeside.foosballranker.view.FailedAuthView;
import dk.lakeside.foosballranker.view.JSonView;
import dk.lakeside.foosballranker.view.View;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ContextTest {

    RequestSource request;
    SessionSource session;
    private Model model;

    @Before
    public void setupSessionAndRequest() {
        model = new Model() {
            @Override
            public Player getPlayer(String id) {
                return new Player("sbv","Simon", "password");
            }

            @Override
            public List<PlayerWithRating> getRankingOfPlayersOpponents(Player player) {
                return new ArrayList<PlayerWithRating>();
            }
        };

        request = new RequestSource() {
            @Override
            public String getParameter(String name) {
                return "sbv";
            }

            @Override
            public String getPayload() {
                return null;
            }
        };

        session = new SessionSource() {
            public String playerId;

            @Override
            public void setCurrentUser(String playerId) {
                this.playerId = playerId;
            }

            @Override
            public String getCurrentUser() {
                return playerId;
            }

            @Override
            public boolean isAuthenticated(Model model) {
                return false;
            }
        };
    }

    @Test
    public void testSubContext() {
        Context context = new Context(null, "liga/lakeside/ranking", null, null);
        Assert.assertEquals("lakeside/ranking", context.subContext().getPathInfo());
    }

    @Test
    public void testWhenNotLoggedInThenServiceReturnsFailedAuthView() throws IOException {
        Context context = new Context(null, "json", request, session);
        Controller controller = new PlayerModstanderController();
        View view = context.service(controller);
        Assert.assertTrue(view instanceof FailedAuthView);
    }

    @Test
    public void testWhenLoggedInThenServiceReturnsJsonView() throws IOException {
        Context context = new Context(model, "json", request, session);
        Controller controller = new PlayerModstanderController();
        session.setCurrentUser("sbv");
        View view = context.service(controller);
        Assert.assertTrue(view instanceof JSonView);
    }
}
