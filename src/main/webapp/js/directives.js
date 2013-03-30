'use strict';

/* Directives */

angular.module('myApp.directives', [])
    .directive('navigationRadio', function($location) {
        return {
            restrict: 'E',
            scope: { model: '=', options:'=', links:'='},
            controller: function($scope){
                $scope.activate = function(option){
                    $scope.model = option;
                    $location.path($scope.links[option]);
                };
            },
            template: "<button type='button' class='btn' "+
                "ng-class='{active: option == model}'"+
                "ng-repeat='option in options' "+
                "ng-click='activate(option)'>{{option}} "+
                "</button>"
        };
    })
    .directive('subnavigationRadio', function($location) {
        return {
            restrict: 'E',
            scope: { model: '=', options:'=', links:'=', show:'='},
            controller: function($scope){
                $scope.activate = function(option){
                    $scope.model = option;
                    $location.path($scope.links[option]);
                };
            },
            template: "<button type='button' class='btn' "+
                "ng-class='{active: option == model}'"+
                "ng-show='show'"+
                "ng-repeat='option in options' "+
                "ng-click='activate(option)'>{{option}} "+
                "</button>"
        };
    })
    .directive('chart', function ($http, auth, $window, $routeParams) {
        return {
            restrict: 'A',
            link: function ($scope, $elm, $attr) {

                var url;
                if($attr['type']=="opponents") {
                    url = 'app/player/'+auth.userId+'/modstandere/chart';
                } else if($attr['type']=="tournament"){
                    url = 'app/player/'+auth.userId+'/turneringer/'+$routeParams.tournamentId+'/ranking/chart';
                }

                $http.get(url)
                    .success(function (data) {

                        var content = data;
                        var chartdata = google.visualization.arrayToDataTable(content.data);
                        drawChart();

                        angular.element($window).bind('resize', function(){
                            $scope.$apply(function() {
                                drawChart();
                            });
                        });

                        function getComputedStyle (element, styleProp) {
                            if (element.currentStyle) {
                                return element.currentStyle[styleProp];
                            } else if ($window.getComputedStyle) {
                                return $window.getComputedStyle(element,null).getPropertyValue(styleProp);
                            }
                            return '';
                        }

                        function drawChart() {
                            var width = parseInt(getComputedStyle($elm[0], 'width'), 10);

                            var options = {
                                'width': width,
                                'height':width/2,
                                'vAxis': {
                                    'baseline': content.baseline,
                                    'minValue': content.baseline,
                                    'maxValue': content.baseline
                                },
                                'chartArea': {
                                    'width': '70%',
                                    'left': 50,
                                    'top': 30
                                },
                                'title': content.title};
                            var chart = new google.visualization.LineChart($elm[0]);
                            chart.draw(chartdata, options);
                        }

                    }).error(function () {
                        //TODO show error
                    });
            }
        }
    });