'use strict';


// Declare app level module which depends on filters, and services
angular.module('myApp', ['myApp.filters', 'myApp.services', 'myApp.directives', 'http-auth-interceptor']).
    directive('authDirective', function($location) {
        return {
            restrict: 'C',
            link: function(scope, elem, attrs) {
                //once Angular is started, remove class:
//                elem.removeClass('waiting-for-angular');

//                var login = elem.find('#login-holder');
//                var main = elem.find('#content');
//
//                login.hide();
                var locationBeforeLogin = null;

                scope.$on('event:auth-loginRequired', function() {
                    locationBeforeLogin = $location.path();
//                    alert("login required "+ $location.path());
                    $location.path('/login');
                    // remember page

//                    login.slideDown('slow', function() {
//                        main.hide();
//                    });
                });
                scope.$on('event:auth-loginConfirmed', function() {
                    //TODO jump to page you are coming from
//                    alert("login confirmed "+locationBeforeLogin);
                    if(locationBeforeLogin != null) {
                        $location.path(locationBeforeLogin);
                        locationBeforeLogin = null;
                    } else {
                        $location.path('/opponents');
                    }
//                    main.show();
//                    login.slideUp();
                });
            }
        }
    }).
  config(['$routeProvider', function($routeProvider) {
    $routeProvider.when('/login', {templateUrl: 'partials/login.html', controller: LoginCtrl});
    $routeProvider.when('/opponents', {templateUrl: 'partials/opponents.html', controller: OpponentsCtrl});
    $routeProvider.when('/opponents/rankingchart', {templateUrl: 'partials/opponents/rankingchart.html', controller: OpponentsCtrl});
    $routeProvider.when('/tournaments', {templateUrl: 'partials/tournaments.html', controller: TournamentsCtrl});
    $routeProvider.when('/changelog', {templateUrl: 'partials/changelog.html', controller: ChangelogCtrl});
    $routeProvider.otherwise({redirectTo: '/opponents'});
  }]).
  config(['$httpProvider', function($httpProvider) {
    // change from post payload to appengine (jetty wont accept payload)
//    $httpProvider.defaults.headers.post['Content-Type']='text/plain;charset=UTF-8';
//    $httpProvider.defaults.headers.post['Accept']='*/*';
//    $httpProvider.defaults.headers.post['X-Requested-With']='';
  }]);

//angular.module('myModule', []).config(['$locationProvider', function($locationProvider) {
//    $locationProvider.html5Mode(true);
