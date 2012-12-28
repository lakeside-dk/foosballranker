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

import com.googlecode.objectify.ObjectifyService;
import dk.lakeside.foosballranker.FreeMarkerHelper;
import dk.lakeside.foosballranker.JSonHelper;
import dk.lakeside.foosballranker.controller.root.BordfodboldController;
import dk.lakeside.foosballranker.controller.Context;
import dk.lakeside.foosballranker.domain.*;
import dk.lakeside.foosballranker.view.*;
import freemarker.template.Configuration;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

public class BordfodboldServlet extends HttpServlet {

    private static final Logger log = Logger.getLogger(BordfodboldServlet.class.getName());

    private Configuration cfg;
    private Model model;

    @Override
    public void init() {
        try {
            //model conf
            model = new Model();

            //freemarker conf
            cfg = new Configuration();
            // loading via classpath works for JettyRunner too
            cfg.setClassForTemplateLoading(this.getClass(), "/templates");
            cfg.setDefaultEncoding("utf-8");

            //objectify conf
            ObjectifyService.register(Player.class);
            ObjectifyService.register(PlayerRelation.class);
            ObjectifyService.register(Match.class);
            ObjectifyService.register(Tournament.class);
            ObjectifyService.register(TournamentRelation.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void service(final HttpServletRequest req, final HttpServletResponse res) throws IOException {
        RequestSource request = new HttpServletRequestSource(req);
        SessionSource session = new HttpServletSessionSource(req);
        Context context = new Context(model, req.getPathInfo().substring(1), request, session);

        View view = context.service(new BordfodboldController());
        if (view != null) {
            view.render(new ViewRenderer() {
                public void show(HtmlView view) {
                    FreeMarkerHelper.showTemplate(view.getTemplate(), res, cfg, view.getAttributes());
                }

                public void show(JSonView view) throws IOException {
                    res.setCharacterEncoding("UTF-8");
                    PrintWriter writer = res.getWriter();
                    String ranking = JSonHelper.toJSon(view.getData());
                    writer.println(ranking);
                }

                public void show(RedirectView redirectView) throws IOException {
//                    log.info("redirecting to "+redirectView.getUrl());
                    res.sendRedirect(redirectView.getUrl());
                }

                public void show(FailedAuthView failedAuthView) throws IOException {
                    res.setStatus(401);
                }

                public void show(CsvView csvView) throws IOException {
                    res.setContentType("text/csv; charset=utf-8");
                    PrintWriter writer = res.getWriter();
                    writer.println(csvView.getCsv());
                }
            });
        }
    }
}
