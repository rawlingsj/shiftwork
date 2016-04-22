(function() {
    'use strict';
    angular
        .module('shiftworkApp')
        .factory('EmployeeDayOffRequest', EmployeeDayOffRequest);

    EmployeeDayOffRequest.$inject = ['$resource'];

    function EmployeeDayOffRequest ($resource) {
        var resourceUrl =  'api/employee-day-off-requests/:id';

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
