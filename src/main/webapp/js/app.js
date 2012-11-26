'use strict';


// Declare app level module which depends on filters, and services
angular.module('myApp', ['myApp.filters', 'myApp.services', 'myApp.directives']).
  config(['$routeProvider', function($routeProvider) {
    $routeProvider.when('/login', {templateUrl: 'partials/login.html', controller: LoginCtrl});
    $routeProvider.when('/opponents', {templateUrl: 'partials/opponents.html', controller: OpponentsCtrl});
    $routeProvider.otherwise({redirectTo: '/login'});
  }]);

//angular.module('myModule', []).config(['$locationProvider', function($locationProvider) {
//    $locationProvider.html5Mode(true);