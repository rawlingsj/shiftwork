(function() {
    'use strict';
    angular
        .module('shiftworkApp')
        .factory('ShiftType', ShiftType);

    ShiftType.$inject = ['$resource'];

    function ShiftType ($resource) {
        var resourceUrl =  'api/shift-types/:id';

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
