(function() {
    'use strict';
    angular
        .module('shiftworkApp')
        .factory('ShiftDate', ShiftDate);

    ShiftDate.$inject = ['$resource'];

    function ShiftDate ($resource) {
        var resourceUrl =  'api/shift-dates/:id';

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
