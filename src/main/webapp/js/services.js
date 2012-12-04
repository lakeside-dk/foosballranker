'use strict';

/* Services */
angular.module('myApp.services', ['ngResource']).
    factory('Opponent', function ($resource) {
        return $resource('data/opponents/opponents.json', {}, {
            query:{method:'GET', params:{}, isArray:true}
        });
    });
