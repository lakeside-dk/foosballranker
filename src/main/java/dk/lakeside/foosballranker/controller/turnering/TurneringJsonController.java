package dk.lakeside.foosballranker.controller.turnering;

import dk.lakeside.foosballranker.controller.Context;
import dk.lakeside.foosballranker.controller.Controller;
import dk.lakeside.foosballranker.domain.Tournament;
import dk.lakeside.foosballranker.view.JSonView;
import dk.lakeside.foosballranker.view.View;

import java.io.IOException;

public class TurneringJsonController implements Controller {
    @Override
    public View service(Context context) throws IOException {
        Tournament tournament = TurneringContext.getTurnering(context);
        return new JSonView(tournament);
    }
}
