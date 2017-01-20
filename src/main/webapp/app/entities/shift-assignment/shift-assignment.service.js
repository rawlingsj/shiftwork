(function() {
    'use strict';
    angular
        .module('shiftworkApp')
        .factory('ShiftAssignment', ShiftAssignment);

    ShiftAssignment.$inject = ['$resource','$q'];


    function ShiftAssignment ($resource,$q) {
        var resourceUrl =  'api/shift-assignments/:id';
         var deferred = $q.defer();

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    deferred.resolve(data);
                    data = angular.fromJson(data);
                    return deferred.promise;
                    //return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
