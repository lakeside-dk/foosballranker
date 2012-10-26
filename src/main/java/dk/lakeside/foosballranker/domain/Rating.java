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

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Indexed;

import javax.persistence.Id;
import java.util.Date;

@Entity
public class Rating
{
    @Id
    private Long id;

    // the time this rating became effective. May be at the creation of a tournament/player, or the time of a match
    private Date time = new Date();

    // the player rated
    private String ratedPlayerId;

    // the new rating
    private int ratingNumber;

    // the rating algorithm used to calculate this rating
    @Indexed
    private Long algorithmId;

    // the tournament. queryable to enable creating charts for a tournament
    @Indexed
    private String tournamentId;

    // the player we wish to generate charts for
    @Indexed
    private String involvedPlayerId;
}
