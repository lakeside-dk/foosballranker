'use strict';

/* Controllers */


function LoginCtrl($scope, $location) {
    $scope.gotoView = function(url) {
        $location.path(url);
//        $location.url = url;
    }
}
LoginCtrl.$inject = ['$scope', '$location'];


function OpponentsCtrl() {
}
OpponentsCtrl.$inject = [];
