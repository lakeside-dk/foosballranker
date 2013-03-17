'use strict';

/* Directives */

angular.module('myApp.directives', [])
    .directive('appVersion', ['version', function (version) {
        return function (scope, elm, attrs) {
            elm.text(version);
        };
    }])

    .directive('opponentschart', function ($http, auth) {
        return {
            restrict: 'A',
            link: function ($scope, $elm, $attr) {

                $http.get('app/player/'+auth.userId+'/modstandere/chart')
                    .success(function (content) {

                        var data = google.visualization.arrayToDataTable(content.data);

                        var options = {
                            'width': 300,
                            'height': 190,
                            'vAxis': {
                        'baseline': content.baseline,
                        'minValue': content.baseline,
                        'maxValue': content.baseline
                            },
                            'chartArea':{
                                'width':200,
                                'height':120,
                                'left':50,
                                'top':30
                            },
                            'title': content.title};


                        var chart = new google.visualization.LineChart($elm[0]);

                        chart.draw(data, options);

                    }).error(function () {
                        //TODO show error
                    });
            }
        }
    })

    .directive('tournamentchart', function ($http, auth, $routeParams) {
        return {
            restrict: 'A',
            link: function ($scope, $elm, $attr) {

                $http.get('app/player/'+auth.userId+'/turneringer/'+$routeParams.tournamentId+'/ranking/chart')
                    .success(function (content) {

                        var data = google.visualization.arrayToDataTable(content.data);

                        var options = {
                            'width': 300,
                            'height': 190,
                            'vAxis': {
                        'baseline': content.baseline,
                        'minValue': content.baseline,
                        'maxValue': content.baseline
                            },
                            'chartArea':{
                                'width':200,
                                'height':120,
                                'left':50,
                                'top':30
                            },
                            'title': content.title};


                        var chart = new google.visualization.LineChart($elm[0]);

                        chart.draw(data, options);

                    }).error(function () {
                        //TODO show error
                    });
            }
        }
    });
