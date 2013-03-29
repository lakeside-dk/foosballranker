'use strict';


// Declare app level module which depends on filters, and services
var app = angular.module('myApp', ['myApp.filters', 'myApp.services', 'myApp.directives', 'http-auth-interceptor', 'analytics'])
    .config(['AnalyticsProvider', function(AnalyticsProvider) {
        AnalyticsProvider.account = 'UA-39369971-1';
    }])
    .config(['$routeProvider', function($routeProvider) {
    $routeProvider.when('/login', {templateUrl: 'partials/login.html', controller: LoginCtrl});
    $routeProvider.when('/opponents', {templateUrl: 'partials/opponents.html', controller: OpponentsCtrl});
    $routeProvider.when('/opponents/rankingchart', {templateUrl: 'partials/opponents/rankingchart.html', controller: OpponentsChartCtrl});
    $routeProvider.when('/tournaments', {templateUrl: 'partials/tournaments.html', controller: TournamentsCtrl});
    $routeProvider.when('/tournaments/:tournamentId', {templateUrl: 'partials/tournaments/tournament.html', controller: TournamentCtrl});
    $routeProvider.when('/tournaments/:tournamentId/addmatch', {templateUrl: 'partials/tournaments/addmatch.html', controller: TournamentAddMatchCtrl});
    $routeProvider.when('/tournaments/:tournamentId/rankingchart', {templateUrl: 'partials/tournaments/rankingchart.html', controller: TournamentChartCtrl});
    $routeProvider.when('/tournaments/:tournamentId/matches', {templateUrl: 'partials/tournaments/matches.html', controller: TournamentMatchesCtrl});
    $routeProvider.when('/changelog', {templateUrl: 'partials/changelog.html', controller: ChangelogCtrl});
    $routeProvider.otherwise({redirectTo: '/login'});
  }])
    .config(['$httpProvider', function($httpProvider) {
        $httpProvider.responseInterceptors.push('HttpLoadingInterceptor');
    // change from post payload to appengine (jetty wont accept payload)
//    $httpProvider.defaults.headers.post['Content-Type']='text/plain;charset=UTF-8';
//    $httpProvider.defaults.headers.post['Accept']='*/*';
//    $httpProvider.defaults.headers.post['X-Requested-With']='';
  }]);

//angular.module('myModule', []).config(['$locationProvider', function($locationProvider) {
//    $locationProvider.html5Mode(true);
