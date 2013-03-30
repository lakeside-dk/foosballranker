'use strict';

/* Services */
angular.module('myApp.services', ['ngResource', 'ngCookies'])

    .service('auth',function Auth($http, $cookieStore, authService) {
        var self = this;
        var cookieName = 'fr-settings-1';
        self.userId = $cookieStore.get(cookieName);

        self.login = function (userId, password) {
            $cookieStore.put(cookieName, userId);
            self.userId = userId;
            $http.post('app/login', {"playerId": userId, "password": password})
                .success(function () {
//                alert("login succes");
                authService.loginConfirmed();
            }).error(function () {
                //TODO show error
            });
        };

        self.logout = function (callback) {
            $cookieStore.put(cookieName, null);
            self.userId = null;
            $http.post('app/logout', {"playerId": self.userId})
                .success(function () {
                    callback();
            }).error(function () {
                //TODO show error
            });
        }
    })

    .service('navigation',function Navigation() {

        self.submodel = "Ranking";
        self.suboptions = ["Ranking", "History"];
        self.path;
    })

    .factory('Player',function ($resource) {
        return $resource('app/player/:userId', {}, {});
    })

    .factory('Opponent',function ($resource) {
        return $resource('app/player/:userId/modstandere/json', {}, {
            query: {method: 'GET', params: {}, isArray: true}
        });
    })
    .service('Tournament',function Tournament($http) {
        var self = this;
        self.get = function (userId, tournamentId) {
            return $http.get('app/player/'+userId+'/turneringer/'+tournamentId+'/json', {});
        };
        self.query = function (userId) {
            return $http.get('app/player/'+userId+'/turneringer/json', {});
        };
    })
    .factory('TournamentOpponent', function ($resource) {
        return $resource('app/player/:userId/turneringer/:turneringId/ranking/json', {}, {
            query: {method: 'GET', params: {}, isArray: true}
        });
    })
    .factory('TournamentMatches', function ($resource) {
        return $resource('app/player/:userId/turneringer/:turneringId/kamp/list', {}, {
            query: {method: 'GET', params: {}, isArray: true}
        });
    });