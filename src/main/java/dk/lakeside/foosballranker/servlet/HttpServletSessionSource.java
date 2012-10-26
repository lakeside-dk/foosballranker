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
package dk.lakeside.foosballranker.servlet;

import com.google.appengine.repackaged.com.google.common.util.Base64;
import com.google.appengine.repackaged.com.google.common.util.Base64DecoderException;
import dk.lakeside.foosballranker.domain.Model;
import dk.lakeside.foosballranker.domain.Player;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.StringTokenizer;

public class HttpServletSessionSource implements SessionSource {
    private HttpServletRequest req;

    public HttpServletSessionSource(HttpServletRequest req) {
        this.req = req;
    }

    public void login(String playerId) {
        getSession().setAttribute("playerid", playerId);
    }

    public boolean isLoggedIn() {
        return currentUser() != null;
    }
    
    public String currentUser() {
        return (String) getSession().getAttribute("playerid");
    }

    public boolean isAuthenticated(Model model) {
        String userID;
        String password;

        // Assume not valid until proven otherwise
        boolean valid = false;

        // Get the Authorization header, if one was supplied
        String authHeader = req.getHeader("Authorization");
        if (authHeader != null) {
            StringTokenizer st = new StringTokenizer(authHeader);
            if (st.hasMoreTokens()) {
                String basic = st.nextToken();

                // We only handle HTTP Basic authentication
                if (basic.equalsIgnoreCase("Basic")) {
                    String credentials = st.nextToken();

                    String userPass;
                    try {
                        userPass = new String(Base64.decode(credentials));

                        // The decoded string is in the form
                        // "userID:password".
                        int p = userPass.indexOf(":");
                        if (p != -1) {
                            userID = userPass.substring(0, p).trim();
                            password = userPass.substring(p+1).trim();

                            //TODO optimize by holding player on session?
                            // Validate user ID and password
                            Player player = model.getPlayer(userID);
                            valid = player != null && player.getPassword().equals(password);
                        }

                    } catch (Base64DecoderException ignored) {
                    }
                }
            }
        }

        // If the user was not validated, fail with a
        // 401 status code (UNAUTHORIZED) and
        // pass back a WWW-Authenticate header for
        // this servlet.
        //
        // Note that this is the normal situation the
        // first time you access the page.  The client
        // web browser will prompt for userID and password
        // and cache them so that it doesn't have to
        // prompt you again.

        return valid;
    }

    private HttpSession getSession() {
        return req.getSession();
    }
}
