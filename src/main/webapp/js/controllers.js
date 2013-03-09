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
    $scope.index = 1;
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


function TournamentCtrl($scope, $routeParams, $location, Tournament, auth) {
    $scope.tournament = Tournament.get({'userId':auth.userId, 'tournamentId':$routeParams.tournamentId});
}
TournamentCtrl.$inject = ['$scope', '$routeParams', '$location', 'Tournament', 'auth'];


function ChangelogCtrl($scope, $location) {
}
ChangelogCtrl.$inject = ['$scope', '$location'];
