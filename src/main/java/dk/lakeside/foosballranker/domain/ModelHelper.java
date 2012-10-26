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
package dk.lakeside.foosballranker.domain;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ModelHelper {
    public static void sortKampeOldestFirst(List<Match> kampe) {
        // sort by timestamp
        Collections.sort(kampe, new Comparator<Match>() {
            public int compare(Match match1, Match match2) {
                return match1.getTime().compareTo(match2.getTime());
            }
        });
    }

    public static void sortKampeNewestFirst(List<Match> kampe) {
        Collections.sort(kampe, new Comparator<Match>() {
            public int compare(Match k1, Match k2) {
                // newest first
                return k2.getTime().compareTo(k1.getTime());
            }
        });
    }

    public static void sortPlayersByName(List<Player> players) {
        Collections.sort(players, new Comparator<Player>() {
            public int compare(Player player1, Player player2) {
                return player1.getName().compareTo(player2.getName());
            }
        });
    }

    public static void sortPlayersByRating(List<Player> ranking) {
        Collections.sort(ranking, new Comparator<Player>() {
            public int compare(Player player1, Player player2) {
                return player2.getRating() - player1.getRating();
            }
        });
    }

    public static void sortTurneringerNewestFirst(List<Tournament> turneringer) {
        Collections.sort(turneringer, new Comparator<Tournament>() {
            public int compare(Tournament t1, Tournament t2) {
                // newest first
                return t2.getStartDate().compareTo(t1.getStartDate());
            }
        });
    }
}
