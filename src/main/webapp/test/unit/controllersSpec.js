'use strict';

/* jasmine specs for controllers go here */
describe('Foosball Ranker controllers', function () {

    describe('OpponentsCtrl', function() {
        var scope, rootScope, ctrl, location;

        beforeEach(inject(function($location, $rootScope, $controller) {
            location = $location;
            rootScope = $rootScope;
            scope = $rootScope.$new();
            ctrl = $controller(OpponentsCtrl, {$scope: scope});
        }));

        it('should change location when setting it via show function', function() {
            location.path('/new/path');
            rootScope.$apply();
            expect(location.path()).toBe('/new/path');

            // test whatever the service should do...
            scope.show('/test');
            expect(location.path()).toBe('/test');

        });
    });

});
