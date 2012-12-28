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

import dk.lakeside.foosballranker.JSonHelper;
import dk.lakeside.foosballranker.controller.player.PlayerContext;
import dk.lakeside.foosballranker.domain.Auth;
import dk.lakeside.foosballranker.domain.Model;
import dk.lakeside.foosballranker.domain.Player;
import dk.lakeside.foosballranker.servlet.RequestSource;
import dk.lakeside.foosballranker.servlet.SessionSource;
import dk.lakeside.foosballranker.view.FailedAuthView;
import dk.lakeside.foosballranker.view.View;

import java.io.IOException;
import java.util.logging.Logger;

public class Context {

    private static final Logger log = Logger.getLogger(Context.class.getName());

    private Model model;
    private String pathInfo;
    private RequestSource request;
    private SessionSource session;

    public Context(Model model, String pathInfo, RequestSource request, SessionSource session) {
        this.model = model;
        this.pathInfo = pathInfo;
        this.request = request;
        this.session = session;
    }
    
    public View service(Controller controller) throws IOException {
        if (controller instanceof SecureController && !loggedIn()) {
            return new FailedAuthView();
        } else if (controller instanceof SecureBasicAuthController && !this.getSession().isAuthenticated(model)) {
            return new FailedAuthView();
        } else {
            return controller.service(this);
        }
    }

    private boolean loggedIn() {
        String playerId = getParameters().getParameter(PlayerContext.getParameterName());
        String currentUser = this.getSession().getCurrentUser();
        return currentUser != null && currentUser.equals(playerId);
    }

    public RequestSource getParameters() {
        return this.request;
    }

    public Model getModel() {
        return model;
    }

    public String getPathInfo() {
        return pathInfo;
    }

    public boolean hasRoot(String name) {
        return pathInfo.startsWith(name);
    }
    
    public Context prepareSubContext(Controller controller) {
        final String subPath = getSubPath();
        final RequestSource subParams;
        if (controller instanceof ParameterizedController) {
            ParameterizedController parameterizedController = (ParameterizedController) controller;
            String parameterName = parameterizedController.getParameterName();
            String parameterValue = root();
            DelegatingRequestSource delSubParams = new DelegatingRequestSource(request);
            delSubParams.setParameter(parameterName, parameterValue);
            subParams = delSubParams;
        } else {
            subParams = request;
        }
        return new Context(model, subPath, subParams, session);
    }

    /**
     * return a new context, with the root of the path removed.
     * @return subcontext
     */
    public Context subContext() {
        final String subPath = getSubPath();
        return new Context(model, subPath, request, session);
    }

    private String getSubPath() {
        final String result;
        if (pathInfo.contains("/")) {
            result = pathInfo.substring(pathInfo.indexOf("/") + 1);
        } else {
            result = "";
        }
        return result;
    }


    public String root() {
        if (pathInfo.contains("/")) {
            return pathInfo.substring(0, pathInfo.indexOf("/"));
        } else {
            return pathInfo;
        }
    }

    public Player login() {
        Auth auth = getObjectFromPostRequest(Auth.class);

        Player player = model.getPlayer(auth.getPlayerId());
        if (auth.getPassword() == null) {
            throw new RuntimeException("Missing password");
        } else if (player == null) {
            throw new RuntimeException("Missing player");
        } else if (!auth.getPassword().equals(player.getPassword())) {
//            log.info("player.id:"+player.getId()+" player.password:"+player.getPassword());
            return null;
        } else {
            session.setCurrentUser(auth.getPlayerId());
            return player;
        }
    }
    
    public String currentUser() {
        return getSession().getCurrentUser();
    }


    public SessionSource getSession() {
        return session;
    }

    public <T> T getObjectFromPostRequest(Class<T> clazz) {
        return JSonHelper.fromJSon(request.getPayload(), clazz);
    }
}
