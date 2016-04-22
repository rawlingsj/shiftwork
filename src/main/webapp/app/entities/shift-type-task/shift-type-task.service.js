(function() {
    'use strict';
    angular
        .module('shiftworkApp')
        .factory('ShiftTypeTask', ShiftTypeTask);

    ShiftTypeTask.$inject = ['$resource'];

    function ShiftTypeTask ($resource) {
        var resourceUrl =  'api/shift-type-tasks/:id';

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
