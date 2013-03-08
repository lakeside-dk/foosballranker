'use strict';

/* Directives */


angular.module('myApp.directives', []).
    directive('appVersion', ['version', function (version) {
        return function (scope, elm, attrs) {
            elm.text(version);
        };
    }]).

    directive('chart', function ($http, auth) {
        return {
            restrict: 'A',
            link: function ($scope, $elm, $attr) {
                // Create the data table.
/*
                var data = new google.visualization.DataTable();
                data.addColumn('string', 'Topping');
                data.addColumn('number', 'Slices');
                data.addRows([
                    ['Mushrooms', 3],
                    ['Onions', 1],
                    ['Olives', 1],
                    ['Zucchini', 1],
                    ['Pepperoni', 2]
                ]);
*/

                $http.get('app/player/'+auth.userId+'/modstandere/chart')
                    .success(function (content) {

                        var data = google.visualization.arrayToDataTable(content.data);

                        var options1 = {'title': 'Company Performance',
                            'width': 400,
                            'height': 300};

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

//                var data = google.visualization.arrayToDataTable($scope.dataArray);

                // Set chart options

/*
                var options2 = {
                    'width': 300,
                    'height': 190,
                    'vAxis': {
//                        'baseline':${baseline?c},
//                        'minValue': ${baseline?c},
//                        'maxValue': ${baseline?c}
                    },
                    'chartArea':{
                        'width':200,
                        'height':120,
                        'left':50,
                        'top':30
                    },
                    'title': '${titel}'};

                // Instantiate and draw our chart, passing in some options.
//                var chart = new google.visualization.PieChart($elm[0]);
                $scope.chart = new google.visualization.LineChart($elm[0]);
//                chart.draw(data, options);
*/
            }
        }
    });
