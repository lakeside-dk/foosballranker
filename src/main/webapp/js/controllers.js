'use strict';

/* Controllers */

function LoginCtrl($scope, $location, auth, Player, $http) {

    $scope.show = function(url) {
        $location.path(url);
    };

    $scope.login = function() {
        auth.login($scope.userId, $scope.password);
    };

    $scope.logout = function() {
        auth.logout(function() {
            $location.path('#/login');
        });
    };

    $scope.createaccount = function() {
        $http.post('app/player/save?name='+$scope.name+'&id='+$scope.newUserId+'&password='+$scope.newPassword, {})
            .success(function () {
                auth.login($scope.newUserId, $scope.newPassword);
//                $location.path('#/opponents');
            }).error(function () {
                //TODO show error
            });

//        var newPlayer = new Player({username:$scope.newUserId});
//        newPlayer.name = "Mike Smith";
//        newPlayer.$save();
//
//        Player.login($scope.userId, $scope.password);
    };
}
LoginCtrl.$inject = ['$scope', '$location', 'auth', 'Player', '$http'];


function OpponentsCtrl($scope, $location, Opponent, auth, $http) {
    $scope.index = 1;
    $scope.opponents = Opponent.query({'userId':auth.userId});
    $scope.name = "";

    $scope.show = function(url) {
        $location.path(url);
    };

    $scope.logout = function() {
        auth.logout(function() {
            //TODO underligt at OpponentsCtrl bliver kaldt igen når location skiftes??? det giver et invalidt kald til Opponent.query som ikke burde ske
            $location.path('#/login');
        });
    };

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

//    $scope.index = 1;
//    $scope.opponents = Opponent.query({'userId':auth.userId});
//    $scope.name = "";
/*
    var dataArray = [
        ['Year', 'Zales', 'Expenses'],
        ['2004',  1000,      400],
        ['2005',  1170,      460],
        ['2006',  660,       1120],
        ['2007',  1030,      540]
    ];
    var data = google.visualization.arrayToDataTable(dataArray);
*/

//    $scope.chart.draw(data, $scope.options);

    $scope.show = function(url) {
        $location.path(url);
    };

    $scope.logout = function() {
        auth.logout(function() {
            $location.path('#/login');
        });
    };

/*
    $scope.linkOpponent = function() {
        $http.post('app/player/'+auth.userId+'/modstandere/add?id='+$scope.userId, {})
            .success(function () {
                $scope.opponents = Opponent.query({'userId':auth.userId});
            }).error(function () {
                //TODO show error
            });
    };
*/
}
OpponentsChartCtrl.$inject = ['$scope', '$location', 'Opponent', 'auth', '$http'];


function TournamentsCtrl($scope, $location, Tournament, auth, $http) {
    $scope.index = 1;
    $scope.tournaments = Tournament.query({'userId':auth.userId});
    $scope.type = 'ranking';

    $scope.show = function(url) {
        $location.path(url);
    };

    $scope.logout = function() {
        auth.logout(function() {
            $location.path('#/login');
        });
    };

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

    $scope.show = function(url) {
        $location.path(url);
    };

    $scope.logout = function() {
        auth.logout(function() {
            $location.path('#/login');
        });
    };
}
TournamentCtrl.$inject = ['$scope', '$routeParams', '$location', 'Tournament', 'auth'];


function ChangelogCtrl($scope, $location) {
    $scope.show = function(url) {
        $location.path(url);
    }
}
ChangelogCtrl.$inject = ['$scope', '$location'];
