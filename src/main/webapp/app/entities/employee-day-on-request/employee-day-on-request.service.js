(function() {
    'use strict';
    angular
        .module('shiftworkApp')
        .factory('EmployeeDayOnRequest', EmployeeDayOnRequest);

    EmployeeDayOnRequest.$inject = ['$resource'];

    function EmployeeDayOnRequest ($resource) {
        var resourceUrl =  'api/employee-day-on-requests/:id';

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
