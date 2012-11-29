'use strict';

/* Controllers */

function LoginCtrl($scope, $location) {
    $scope.show = function(url) {
        $location.path(url);
    }
}
LoginCtrl.$inject = ['$scope', '$location'];


function OpponentsCtrl($scope, $location) {
    $scope.show = function(url) {
        $location.path(url);
    }
}
OpponentsCtrl.$inject = ['$scope', '$location'];

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
