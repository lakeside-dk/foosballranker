'use strict';

app.factory('HttpLoadingInterceptor', [
    '$rootScope', '$q',
    function($rootScope, $q) {
        $rootScope.pending = 0;
        return function(promise) {
            if ($rootScope.pending === 0) {
                $rootScope.$broadcast('event:http:loading');
            }
            $rootScope.pending++;
            return promise.then(function(response) {
                $rootScope.pending--;
                if ($rootScope.pending === 0) {
                    $rootScope.$broadcast('event:http:loaded');
                }
                return response;
            }, function(response) {
                $rootScope.pending--;
                if ($rootScope.pending === 0) {
                    $rootScope.$broadcast('event:http:loaded');
                }
                return $q.reject(response);
            });
        };
    }
]);