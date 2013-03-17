'use strict';

/* Controllers */

function MainCntl($scope, $location, auth) {

    $scope.show = function(url) {
        $location.path(url);
    };

    $scope.logout = function() {
        auth.logout(function() {
            $location.path('#/login');
        });
    };
}
MainCntl.$inject = ['$scope', '$location', 'auth'];

function LoginCtrl($scope, $location, auth, Player, $http) {

    $scope.login = function() {
        auth.login($scope.userId, $scope.password);
    };

    $scope.createaccount = function() {
        $http.post('app/player/save?name='+$scope.name+'&id='+$scope.newUserId+'&password='+$scope.newPassword, {})
            .success(function () {
                auth.login($scope.newUserId, $scope.newPassword);
            }).error(function () {
                //TODO show error
            });
    };
}
LoginCtrl.$inject = ['$scope', '$location', 'auth', 'Player', '$http'];


function OpponentsCtrl($scope, $location, Opponent, auth, $http) {
    $scope.opponents = Opponent.query({'userId':auth.userId});
    $scope.name = "";

    $scope.linkOpponent = function() {
        $http.post('app/player/'+auth.userId+'/modstandere/add?id='+$scope.userId, {})
            .success(function () {
                $scope.opponents = Opponent.query({'userId':auth.userId});
            }).error(function () {
                //TODO show error
            });
    };
}
OpponentsCtrl.$inject = ['$scope', '$location', 'Opponent', 'auth', '$http'];


function OpponentsChartCtrl($scope, $location, Opponent, auth, $http) {
}
OpponentsChartCtrl.$inject = ['$scope', '$location', 'Opponent', 'auth', '$http'];


function TournamentsCtrl($scope, $location, Tournament, auth, $http) {
    $scope.index = 1;
    $scope.tournaments = Tournament.query({'userId':auth.userId});
    $scope.type = 'ranking';

    $scope.createTournament = function() {
        $http.post('app/player/'+auth.userId+'/turneringer/save?type='+$scope.type+'&name='+$scope.name, {})
            .success(function () {
                $scope.tournaments = Tournament.query({'userId':auth.userId});
            }).error(function () {
                //TODO show error
            });
    };
}
TournamentsCtrl.$inject = ['$scope', '$location', 'Tournament', 'auth', '$http'];


function TournamentCtrl($scope, $routeParams, $location, TournamentOpponent, auth, $http) {
    $scope.tournamentId = $routeParams.tournamentId;
    $scope.opponents = TournamentOpponent.query({'userId':auth.userId, 'turneringId':$routeParams.tournamentId});
}
TournamentCtrl.$inject = ['$scope', '$routeParams', '$location', 'TournamentOpponent', 'auth', '$http'];


function TournamentAddMatchCtrl($scope, $routeParams, $location, Tournament, auth, $http) {

    var ONE_ON_ONE = '1 vs. 1';
    var TWO_ON_TWO = '2 vs. 2';

    init();

    function init() {
        $scope.playersSelected = 0;
        $scope.blueScoreSelected = false;
        $scope.redScoreSelected = false;
        $scope.selectedPlayerIds = [];

        $scope.gametype = 'Select type of match';
        $scope.showGametypeButtons = true;
        $scope.showPlayers = false;
        $scope.showResult = false;
        $scope.showSave = false;

        $scope.blueScores = [];
        $scope.redScores = [];
        for (var i = 0; i < 11; i++) {
            $scope.blueScores[i] = {};
            $scope.blueScores[i].value = i;
            $scope.redScores[i] = {};
            $scope.redScores[i].value = i;
        }

        $http.get('app/player/'+auth.userId+'/turneringer/'+$routeParams.tournamentId+'/kamp/data')
            .success(function (data) {
                $scope.players = data["players"];
                for (var i = 0; i < $scope.players.length; i++) {
                    $scope.players[i].role = '';
                }
            }).error(function () {
                //TODO show error
            });
    }

    $scope.selectOneVsOne = function() {
        $scope.gametype = ONE_ON_ONE;
        $scope.showGametypeButtons = false;
        $scope.showPlayers = true;
    };

    $scope.selectTwoVsTwo = function() {
        $scope.gametype = TWO_ON_TWO;
        $scope.showGametypeButtons = false;
        $scope.showPlayers = true;
    };

    $scope.computePlayerShow = function(player) {
        if(player.selected) {
            return true;
        } else {
            if($scope.gametype == ONE_ON_ONE && $scope.playersSelected == 2
                || $scope.gametype == TWO_ON_TWO && $scope.playersSelected == 4) {
                return false;
            } else {
                return true;
            }
        }
    }

    $scope.computeBlueScoreShow = function(score) {
        return !$scope.blueScoreSelected || score.selected;
    };

    $scope.computeRedScoreShow = function(score) {
        return !$scope.redScoreSelected || score.selected;
    };

    $scope.computePlayerClass = function(player) {
        var result = 'btn';
        if(player.selected) {
            result += ' disabled';
            if($scope.gametype == ONE_ON_ONE && player.selected < 2
                || $scope.gametype == TWO_ON_TWO && player.selected < 3) {
                    result += ' btn-primary';
            } else {
                result += ' btn-danger';
            }
        }
        return result;
    };

    $scope.computeBlueScoreClass = function(score) {
        var result = 'btn btn-primary';
        if(score.selected) {
            result += ' disabled';
        }
        return result;
    };

    $scope.computeRedScoreClass = function(score) {
        var result = 'btn btn-danger';
        if(score.selected) {
            result += ' disabled';
        }
        return result;
    };

    $scope.selectPlayer = function(player) {
        player.selected = ++($scope.playersSelected);
        $scope.selectedPlayerIds.push(player.id);

        if($scope.gametype == ONE_ON_ONE && $scope.playersSelected == 2
            || $scope.gametype == TWO_ON_TWO && $scope.playersSelected == 4) {
            $scope.showResult = true;
        }
    };

    $scope.selectBlueScore = function(score) {
        $scope.blueScoreSelected = true;
        $scope.blueScoreValue = score.value;
        score.selected = true;
        if($scope.blueScoreSelected && $scope.redScoreSelected) {
            $scope.showSave = true;
        }
    };

    $scope.selectRedScore = function(score) {
        $scope.redScoreSelected = true;
        $scope.redScoreValue = score.value;
        score.selected = true;
        if($scope.blueScoreSelected && $scope.redScoreSelected) {
            $scope.showSave = true;
        }
    };

    $scope.reset = function() {
        init();
    };

    $scope.save = function() {
        var data = {"createdById":auth.userId,"playerIds":$scope.selectedPlayerIds,"score1":$scope.blueScoreValue,"score2":$scope.redScoreValue};
        $http.post('app/player/'+auth.userId+'/turneringer/'+$routeParams.tournamentId+'/kamp/add', data)
            .success(function () {
                init();
            }).error(function () {
                //TODO show error
            });
    };
}
TournamentAddMatchCtrl.$inject = ['$scope', '$routeParams', '$location', 'Tournament', 'auth', '$http'];


function TournamentChartCtrl($scope, $location, Opponent, auth, $http) {
}
TournamentChartCtrl.$inject = ['$scope', '$location', 'Opponent', 'auth', '$http'];


function TournamentMatchesCtrl($scope, TournamentMatches, auth, $routeParams) {
    $scope.matches = TournamentMatches.query({'userId':auth.userId, 'turneringId':$routeParams.tournamentId});
}
TournamentMatchesCtrl.$inject = ['$scope', 'TournamentMatches', 'auth', '$routeParams'];


function ChangelogCtrl($scope, $location) {
}
ChangelogCtrl.$inject = ['$scope', '$location'];
