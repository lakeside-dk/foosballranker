'use strict';


// Declare app level module which depends on filters, and services
angular.module('myApp', ['myApp.filters', 'myApp.services', 'myApp.directives']).
  config(['$routeProvider', function($routeProvider) {
    $routeProvider.when('/login', {templateUrl: 'partials/login.html', controller: LoginCtrl});
    $routeProvider.when('/opponents', {templateUrl: 'partials/opponents.html', controller: OpponentsCtrl});
    $routeProvider.when('/opponents/rankingchart', {templateUrl: 'partials/opponents/rankingchart.html', controller: OpponentsCtrl});
    $routeProvider.when('/tournaments', {templateUrl: 'partials/tournaments.html', controller: TournamentsCtrl});
    $routeProvider.when('/changelog', {templateUrl: 'partials/changelog.html', controller: ChangelogCtrl});
    $routeProvider.otherwise({redirectTo: '/login'});
  }]);

//angular.module('myModule', []).config(['$locationProvider', function($locationProvider) {
//    $locationProvider.html5Mode(true);