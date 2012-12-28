package dk.lakeside.foosballranker.domain;

public class PlayerWithRating extends Player {
    public PlayerWithRating(Player p, Integer rating) {
        super(p.getId(), p.getName(), rating);
    }
}
