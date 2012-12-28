'use strict';

/* Services */
angular.module('myApp.services', ['ngResource','ngCookies']).

    service('auth', function Auth($http, $cookieStore, authService) {

        var self = this;

        self.userId = $cookieStore.get('userId');

        self.login = function(userId, password) {
            $cookieStore.put('userId', userId);
            self.userId = userId;
            $http.post('app/login',{"playerId": userId, "password": password}).success(function() {
//                Player.get({userId:})
                authService.loginConfirmed();
//                alert('success');
            });
        }
    }).
    factory('Player', function ($resource) {
//        return $resource('data/opponents.json', {}, {
        return $resource('app/player/:userId/modstandere/json', {}, {
            query:{method:'GET', params:{}, isArray:true}
        });
    }).
    factory('Opponent', function ($resource) {
//        return $resource('data/opponents.json', {}, {
        return $resource('app/player/:userId/modstandere/json', {}, {
            query:{method:'GET', params:{}, isArray:true}
        });
    }).
    factory('Tournament', function ($resource) {
//        return $resource('data/tournaments.json', {}, {
        return $resource('app/player/:userId/turneringer/json', {}, {
            query:{method:'GET', params:{}, isArray:true}
        });
    });
