(function() {
    'use strict';
    angular
        .module('shiftworkApp')
        .factory('ShiftAssignment', ShiftAssignment);

    ShiftAssignment.$inject = ['$resource'];

    function ShiftAssignment ($resource) {
        var resourceUrl =  'api/shift-assignments/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
