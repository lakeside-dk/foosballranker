'use strict';

/* Controllers */

function LoginCtrl($scope, $location, auth) {

    $scope.show = function(url) {
        $location.path(url);
    }

    $scope.login = function() {
//        alert('test1 ');
//        Auth.userId = $scope.userId;
//        Auth.password = $scope.password;
        //TODO put player on global scope or as a service?
        auth.login($scope.userId, $scope.password);
//        alert('test2 '+$scope.loggedIn);
//        $scope.opponents.push({"name": $scope.name,"username": "sbv","rating": 0});
    };

}
LoginCtrl.$inject = ['$scope', '$location', 'auth'];


function OpponentsCtrl($scope, $location, Opponent, auth) {
    $scope.index = 1;
//    $scope.userId = 'simon';
    $scope.opponents = Opponent.query({'userId':auth.userId});
    $scope.name = "";

    $scope.show = function(url) {
        $location.path(url);
    };

    $scope.linkOpponent = function() {
        $scope.opponents.push({"name": $scope.name,"username": "sbv","rating": 0});
    };

/*
    $scope.addOpponent = function() {
        //fetch user from server before pushing
        var opponent = Opponent.requestLink($scope.username);
        $scope.opponents.push(opponent);
    };
*/

}
OpponentsCtrl.$inject = ['$scope', '$location', 'Opponent', 'auth'];


function TournamentsCtrl($scope, $location, Tournament, auth) {
    $scope.index = 1;
    $scope.tournaments = Tournament.query({'userId':auth.userId});
    $scope.type = 'ranking';

    $scope.show = function(url) {
        $location.path(url);
    }
}
TournamentsCtrl.$inject = ['$scope', '$location', 'Tournament', 'auth'];


function ChangelogCtrl($scope, $location) {
    $scope.show = function(url) {
        $location.path(url);
    }
}
ChangelogCtrl.$inject = ['$scope', '$location'];
