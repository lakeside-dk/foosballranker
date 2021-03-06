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
import dk.lakeside.foosballranker.domain.Auth;
import dk.lakeside.foosballranker.domain.Player;
import dk.lakeside.foosballranker.servlet.RequestSource;
import dk.lakeside.foosballranker.view.JSonView;
import dk.lakeside.foosballranker.view.View;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

public class PlayerSendResetPasswordEmailController implements Controller {

    public View service(final Context context) throws IOException {
        RequestSource params = context.getParameters();
        String email = params.getParameter("email");

        Player player = context.getModel().getPlayerByEmail(email);
        if(player == null) {
            throw new RuntimeException("player not found");
        }
        String hash = Auth.getHash(player.getPassword());
        String playerId = player.getId();


        sendMail(email, player.getName(), hash, playerId);

        return new JSonView("");
    }

    private void sendMail(String email, String name, String hash, String playerId) throws UnsupportedEncodingException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        String msgBody = "Click the link to reset your password http://foosballranker.appspot.com/app/player/sendnewpasswordemail?playerId="+playerId+"&hash="+hash+" - an email will be sent with your new password..";
        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("noreply@foosballranker.appspotmail.com"));
            msg.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(email, name));
            msg.setSubject("Password reset request from Foosball Ranker");
            msg.setText(msgBody);
            Transport.send(msg);

        } catch (AddressException e) {
            // ...
        } catch (MessagingException e) {
            // ...
        }
    }
}
