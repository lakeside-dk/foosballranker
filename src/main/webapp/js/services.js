'use strict';

/* Services */
angular.module('myApp.services', ['ngResource', 'ngCookies']).

    service('auth',function Auth($http, $cookieStore, authService) {

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
        }

        self.logout = function (callback) {
            $http.post('app/logout', {"playerId": self.userId})
                .success(function () {
                    $cookieStore.put(cookieName, null);
                    self.userId = null;
                    callback();
            }).error(function () {
                //TODO show error
            });
        }
    }).
    factory('Player',function ($resource) {
        return $resource('app/player/:userId', {}, {});
    }).
    factory('Opponent',function ($resource) {
        return $resource('app/player/:userId/modstandere/json', {}, {
            query: {method: 'GET', params: {}, isArray: true}
        });
    }).
    factory('Tournament', function ($resource) {
        return $resource('app/player/:userId/turneringer/json', {}, {
            query: {method: 'GET', params: {}, isArray: true}
        });
    }).
    factory('TournamentOpponent', function ($resource) {
        return $resource('app/player/:userId/turneringer/:turneringId/ranking/json', {}, {
            query: {method: 'GET', params: {}, isArray: true}
        });
    }).
    factory('TournamentMatches', function ($resource) {
        return $resource('app/player/:userId/turneringer/:turneringId/kamp/list', {}, {
            query: {method: 'GET', params: {}, isArray: true}
        });
    });
