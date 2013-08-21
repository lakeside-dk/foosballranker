'use strict';

/* Controllers */

function MainCntl($rootScope, $scope, $location, auth, $routeParams) {

    $scope.navigationLevelOne = ["Opponents", "Tournaments"];
    $scope.navigationLinksLevelOne = {"Opponents":"/opponents", "Tournaments":"/tournaments"};
    $scope.navigationLevelTwoShow = true;

    $scope.navigationLevelTwoOpponent = ["Ranking", "History"];
    $scope.createOpponentNavigationLinks = function() {
        $scope.navigationLinksLevelTwo = {"Ranking":"/opponents", "History":"/opponents/rankingchart"};
    };

    $scope.navigationLevelTwoTournament = ["Ranking", "History","New Match","Matches"];
    $scope.createTournamentNavigationLinks = function(tournamentId) {
        $scope.navigationLinksLevelTwo = {
        "Ranking":"/tournaments/"+tournamentId,
        "History":"/tournaments/"+tournamentId+"/rankingchart",
        "New Match":"/tournaments/"+tournamentId+"/addmatch",
        "Matches":"/tournaments/"+tournamentId+"/matches"};
    };

    // Show or hide loading indicator
    $scope.loading = false;
    $scope.$on('event:http:loading', function() {
        $scope.loading = true;
    });
    $scope.$on('event:http:loaded', function() {
        $scope.loading = false;
    });

    // Handle auth
    var locationBeforeLogin = null;
    $scope.$on('event:auth-loginRequired', function() {
        locationBeforeLogin = $location.path();
        auth.userId = null;
        $location.path('/login');
    });
    $scope.$on('event:auth-loginConfirmed', function() {
        //TODO jump to page you are coming from
        if(locationBeforeLogin != null) {
            $location.path(locationBeforeLogin);
            locationBeforeLogin = null;
        } else {
            $location.path('/opponents');
        }
    });

    // handle warnings
    $scope.showwarning = false;
    $scope.closewarning = function() {
        $scope.showwarning = false;
    };
    $scope.warn = function(txt) {
        $scope.warningtext = txt;
        $scope.showwarning = true;
    };
    $rootScope.$on('$viewContentLoaded', function() {
        $scope.showwarning = false;
    });

    $scope.show = function(url) {
        $location.path(url);
    };

    $scope.logout = function() {
        auth.logout().success(function () {
            $location.path('#/login');
        }).error(function () {
            $scope.warn("Logout failed.");
        });
    };
}
MainCntl.$inject = ['$rootScope', '$scope', '$location', 'auth','$routeParams'];


function LoginCtrl($scope, $location, auth, Player, $http) {

    $scope.loginVisible=true;
    $scope.resetPasswordVisible=false;
    $scope.createAccountVisible=false;

    if(auth.userId != null) {
        $location.path('/opponents');
    }

    $scope.login = function() {
        auth.login($scope.userId, $scope.password, function() {
            $scope.warn("Login failed.");
        });
    };

    $scope.showResetPassword = function() {
      $scope.resetPasswordVisible = true;
//      $scope.loginVisible = false;
    };

    $scope.showCreateAccount = function() {
      $scope.createAccountVisible = true;
//      $scope.loginVisible = false;
    };

    $scope.createaccount = function() {
        $http.post('app/player/save?name='+$scope.name+'&id='+$scope.newUserId+'&password='+$scope.newPassword+'&email='+$scope.email, {})
            .success(function () {
                auth.login($scope.newUserId, $scope.newPassword);
            }).error(function () {
                $scope.warn("Create account failed.");
            });
    };

    $scope.resetPassword = function() {
        $http.post('app/player/sendresetpasswordemail?email='+$scope.resetemail, {})
            .success(function () {
            }).error(function () {
                $scope.warn("reset failed");
            });
    };
}
LoginCtrl.$inject = ['$scope', '$location', 'auth', 'Player', '$http'];


function OpponentsCtrl($scope, $location, Opponent, auth, $http) {
    $scope.navigationLevelTwo = $scope.navigationLevelTwoOpponent;
    $scope.createOpponentNavigationLinks();
    $scope.navigationLevelOneSelected = 'Opponents';
    $scope.navigationLevelTwoSelected = 'Ranking';

    $scope.opponents = Opponent.query({'userId':auth.userId});
    $scope.name = "";

    $scope.linkOpponent = function() {
        $http.post('app/player/'+auth.userId+'/modstandere/add?id='+$scope.userId, {})
            .success(function () {
                $scope.opponents = Opponent.query({'userId':auth.userId});
            }).error(function () {
                $scope.warn("Link to opponent failed.");
            });
    };

  $scope.remove = function(id) {
    $http.post('app/player/'+auth.userId+'/modstandere/remove?id='+id, {})
        .success(function () {
          $scope.opponents = Opponent.query({'userId':auth.userId});
        }).error(function () {
          $scope.warn("Remove opponent failed.");
        });
  };

  $scope.show = function(id) {
    return id != auth.userId;
  };

}
OpponentsCtrl.$inject = ['$scope', '$location', 'Opponent', 'auth', '$http'];


function OpponentsChartCtrl($scope, $location, Opponent, auth, $http) {
    $scope.navigationLevelTwo = $scope.navigationLevelTwoOpponent;
    $scope.createOpponentNavigationLinks();
    $scope.navigationLevelOneSelected = 'Opponents';
    $scope.navigationLevelTwoSelected = "History";
}
OpponentsChartCtrl.$inject = ['$scope', '$location', 'Opponent', 'auth', '$http'];


function TournamentsCtrl($scope, Tournament, auth, $http) {
    $scope.navigationLevelOneSelected = 'Tournaments';
    $scope.navigationLevelTwoShow = false;

    $scope.index = 1;
    $scope.type = 'ranking';

    getTournaments();

    function getTournaments() {
        Tournament.query(auth.userId)
            .success(function (result) {
                $scope.tournaments = result;
            }).error(function () {
                $scope.warn("Data request failed.");
            });
    }

    $scope.createTournament = function() {
        $http.post('app/player/'+auth.userId+'/turneringer/save?type='+$scope.type+'&name='+$scope.name, {})
            .success(function () {
                getTournaments();
            }).error(function () {
                $scope.warn("Add tournament failed.");
            });
    };
}
TournamentsCtrl.$inject = ['$scope', 'Tournament', 'auth', '$http'];


function TournamentCtrl($scope, $routeParams, Tournament, TournamentOpponent, auth, $http) {
    $scope.navigationLevelTwo = $scope.navigationLevelTwoTournament;
    $scope.createTournamentNavigationLinks($routeParams.tournamentId);
    $scope.navigationLevelOneSelected = '';
    $scope.navigationLevelTwoSelected = "Ranking";

    $scope.tournamentId = $routeParams.tournamentId;

    getTournament();

    function getTournament() {
        Tournament.get(auth.userId, $scope.tournamentId)
            .success(function (result) {
                $scope.tournament = result;
                $scope.statusText = $scope.tournament.endDate == null?'Close':'Open';
            }).error(function () {
                $scope.warn("Data request failed.");
            });
    }

    $scope.opponents = TournamentOpponent.query({'userId':auth.userId, 'turneringId':$routeParams.tournamentId});

    $scope.changeStatus = function() {
        if( $scope.tournament.endDate == null) {
            $http.post('app/player/'+auth.userId+'/turneringer/'+$scope.tournamentId+'/afslut', {})
                .success(function () {
                    getTournament();
                }).error(function () {
                    $scope.warn("Close failed.");
                });
        } else {
            $http.post('app/player/'+auth.userId+'/turneringer/'+$scope.tournamentId+'/genaabn', {})
                .success(function () {
                    getTournament();
                }).error(function () {
                    $scope.warn("Open failed.");
                });
        }
    };
}
TournamentCtrl.$inject = ['$scope', '$routeParams', 'Tournament', 'TournamentOpponent', 'auth', '$http'];


function TournamentAddMatchCtrl($scope, $routeParams, Tournament, auth, $http) {
    $scope.navigationLevelOneSelected = '';
    $scope.navigationLevelTwo = $scope.navigationLevelTwoTournament;
    $scope.createTournamentNavigationLinks($routeParams.tournamentId);
    $scope.navigationLevelTwoSelected = "New Match";

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

        Tournament.get(auth.userId, $routeParams.tournamentId)
            .success(function (result) {
                $scope.tournament = result;
            }).error(function () {
                $scope.warn("Data request failed.");
            });

        $http.get('app/player/'+auth.userId+'/turneringer/'+$routeParams.tournamentId+'/kamp/data')
            .success(function (data) {
                $scope.players = data["players"];
                for (var i = 0; i < $scope.players.length; i++) {
                    $scope.players[i].role = '';
                }
            }).error(function () {
                $scope.warn("Data request failed.");
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

    $scope.computePlayersDisabled = function(player) {
        return ($scope.gametype == ONE_ON_ONE && $scope.playersSelected == 2
            || $scope.gametype == TWO_ON_TWO && $scope.playersSelected == 4
            || player.selected);
    };

    $scope.computeBlueScoreDisabled = function() {
        return $scope.blueScoreSelected;
    };

    $scope.computeRedScoreDisabled = function() {
        return $scope.redScoreSelected;
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
                $scope.warn("Register match failed.");
            });
    };
}
TournamentAddMatchCtrl.$inject = ['$scope', '$routeParams', 'Tournament', 'auth', '$http'];


function TournamentChartCtrl($scope, Tournament, auth, $routeParams) {
    $scope.navigationLevelOneSelected = '';
    $scope.navigationLevelTwo = $scope.navigationLevelTwoTournament;
    $scope.createTournamentNavigationLinks($routeParams.tournamentId);
    $scope.navigationLevelTwoSelected = "History";

    Tournament.get(auth.userId, $routeParams.tournamentId)
        .success(function (result) {
            $scope.tournament = result;
        }).error(function () {
            $scope.warn("Data request failed.");
        });
}
TournamentChartCtrl.$inject = ['$scope', 'Tournament', 'auth', '$routeParams'];


function TournamentMatchesCtrl($scope, TournamentMatches, auth, $routeParams, $http, Tournament) {
    $scope.navigationLevelOneSelected = '';
    $scope.navigationLevelTwo = $scope.navigationLevelTwoTournament;
    $scope.createTournamentNavigationLinks($routeParams.tournamentId);
    $scope.navigationLevelTwoSelected = "Matches";

    Tournament.get(auth.userId, $routeParams.tournamentId)
        .success(function (result) {
            $scope.tournament = result;
        }).error(function () {
            $scope.warn("Data request failed.");
        });

    $scope.matches = TournamentMatches.query({'userId':auth.userId, 'turneringId':$routeParams.tournamentId});

    $scope.delete = function(id) {
        $http.post('app/player/'+auth.userId+'/turneringer/'+$routeParams.tournamentId+'/kamp/delete?id='+id, {})
            .success(function () {
                $scope.matches = TournamentMatches.query({'userId':auth.userId, 'turneringId':$routeParams.tournamentId});
            }).error(function () {
                $scope.warn("Delete failed.");
            });
    };
}
TournamentMatchesCtrl.$inject = ['$scope', 'TournamentMatches', 'auth', '$routeParams', '$http', 'Tournament'];


function ChangelogCtrl($scope, $location) {
}
ChangelogCtrl.$inject = ['$scope', '$location'];
