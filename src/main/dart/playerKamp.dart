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
#library('playerKamp');

#import('dart:html');
#import("dart:json");

Map matchData;
List players;
var isSingleMatch;

main() {

  matchData = new Map();

  document.query('#onevsone').on.click.add((MouseEvent e) {
    handleType();
    document.query('#pickedgametype').innerHTML = '1 vs. 1';
    isSingleMatch = true;
  });

  document.query('#twovstwo').on.click.add((MouseEvent e) {
    handleType();
    document.query('#pickedgametype').innerHTML = '2 vs. 2';
    isSingleMatch = false;
  });

  document.query('#savebutton').on.click.add((MouseEvent e) {
    document.query('#savebutton').style.setProperty("display", "none");
    sendRequest("add",
      matchData,
      (Map response) => onSave(response),
      () => onSaveFailed());
  });

// get players and turneringer
  var _dataRequest = new Map();
  sendRequest("data",
  _dataRequest,

      (Map response) => onData(response),

      () => onDataFailed());

// setup score buttons
  for (var i = 0; i <= 10; i++) {
    var clearleft = "";
    if (i == 10 || (i > 0 && i % 5 == 0)) {
      clearleft = "clearleft";
    }
    var score1 = new Element.html('<button id="red$i" disabled="disabled" class="scorebutton red $clearleft">$i</button>');
    document.query('#redscore').elements.add(score1);
    score1.on.click.add((MouseEvent e) {
      document.query('#redscore').style.setProperty("display", "none");
      document.query('#pickedredscore').elements.add(score1);
      score1.disabled = true;
      matchData['score1'] = i;
      enableScoreButtons('blue');
    });
    var score2 = new Element.html('<button id="blue$i" disabled="disabled" class="scorebutton blue $clearleft">$i</button>');
    document.query('#bluescore').elements.add(score2);
    score2.on.click.add((MouseEvent e) {
      document.query('#bluescore').style.setProperty("display", "none");
      document.query('#pickscore').innerHTML = 'Kampens resultat';
      document.query('#pickedbluescore').elements.add(score2);
      score2.disabled = true;
      matchData['score2'] = i;
      document.query('#savebutton').disabled = false;
    });
  }
}

void handleType() {
  document.query('#pickgametype').innerHTML = 'Type';
  document.query('#gametypes').style.setProperty("display", "none");
  for (var i = 0; i < players.length; i++) {
    var id = players[i]['id'];
    document.query('#$id').disabled = false;
  }
}

void onSave(Map response) {
  document.query('#status').innerHTML = 'Match saved';
}

void onSaveFailed() {
  document.query('#status').innerHTML = 'Save failed!';
}

void onData(Map response) {

  matchData['createdById'] = response["player"];

  players = response["players"];
  List _pickedPlayers = new List();

  for (var i = 0; i < players.length; i++) {
    var name = players[i]["name"];
    var id = players[i]["id"];
    var player;
    var clearleft = "";
    if (i > 0 && i % 2 == 0) {
      clearleft = "clearleft";
    }
    player = new Element.html('<button disabled="disabled" id="$id" class="button $clearleft">$name</button>');
    document.query('#players').elements.add(player);

    player.on.click.add((MouseEvent e) {
      if (isSingleMatch && _pickedPlayers.length < 1
      || !isSingleMatch && _pickedPlayers.length < 2) {
        player.style.setProperty("background-color", "#ffbabf");
      } else {
        player.style.setProperty("background-color", "#66B3FF");
      }
      _pickedPlayers.add(player);

// if doublematch add attacker and defense icons
      if (!isSingleMatch) {
        if (_pickedPlayers.length == 1 || _pickedPlayers.length == 4) {
          player.innerHTML = '$name (attacker)';
        } else {
          player.innerHTML = '$name (defender)';
        }
      }
      player.disabled = true;

// if all selected - remove the rest
      if (isSingleMatch && _pickedPlayers.length == 2
      || !isSingleMatch && _pickedPlayers.length == 4) {
        document.query('#players').style.setProperty("display", "none");
        var playerIds = new List();
        for (var j = 0; j < _pickedPlayers.length; j++) {
          _pickedPlayers[j].remove();
          _pickedPlayers[j].classes.remove('clearleft');
          if (j > 0 && j % 2 == 0) {
            _pickedPlayers[j].classes.add('clearleft');
          }
          document.query('#pickedplayers').elements.add(_pickedPlayers[j]);
          document.query('#pickplayers').innerHTML = 'Spillere';
          playerIds.add(_pickedPlayers[j].id);
        }
// update data
        var index = _pickedPlayers.length;
        matchData['playerIds'] = playerIds;
// enable red buttons
        enableScoreButtons('red');
      }
    });
  }
}

void enableScoreButtons(var color) {
  for (var i = 0; i <= 10; i++) {
    document.query('#$color$i').disabled = false;
  }
}

void onDataFailed() {
  document.query('#status').innerHTML = "Failed to connect. Please try again later.";
}

HttpRequest sendRequest(String url, Map json, var onSuccess, var onError) {
  HttpRequest request = new HttpRequest();
  request.on.readyStateChange.add((Event event) {
    if (request.readyState != 4) return;
    if (request.status == 200) {
      onSuccess(JSON.parse(request.responseText));
    } else {
      onError();
    }
  });
  request.open("POST", url, true);
  request.setRequestHeader("Content-Type", "text/plain;charset=UTF-8");
  request.send(JSON.stringify(json));
  return request;
}

class FRMatch {
  int score1;
  int score2;
  String createdById;
  String turneringId;
  List<String> playerIds;
}