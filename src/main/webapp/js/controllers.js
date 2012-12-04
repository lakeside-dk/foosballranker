'use strict';

/* Controllers */

function LoginCtrl($scope, $location) {
    $scope.show = function(url) {
        $location.path(url);
    }
}
LoginCtrl.$inject = ['$scope', '$location'];

function OpponentsCtrl($scope, $location, Opponent) {
    $scope.index = 1;
    $scope.opponents = Opponent.query();

    $scope.show = function(url) {
        $location.path(url);
    };

    $scope.getPosition = function() {
        return $scope.index++;
    };
}
OpponentsCtrl.$inject = ['$scope', '$location', 'Opponent'];

function TournamentsCtrl($scope, $location) {
    $scope.show = function(url) {
        $location.path(url);
    }
}
TournamentsCtrl.$inject = ['$scope', '$location'];

function ChangelogCtrl($scope, $location) {
    $scope.show = function(url) {
        $location.path(url);
    }
}
ChangelogCtrl.$inject = ['$scope', '$location'];
