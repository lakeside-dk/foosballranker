'use strict';

/* Filters */

angular.module('myApp.filters', []).
  filter('players', function() {
    return function(text) {
        if(text.length == 2) return text[0] + " vs. "+text[1];
        else if(text.length == 4) return text[0] + ", "+text[1] + " vs. "+ text[2] + ", "+text[3];
        else return "";
    }
  });
