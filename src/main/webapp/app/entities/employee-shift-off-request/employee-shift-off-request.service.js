(function() {
    'use strict';
    angular
        .module('shiftworkApp')
        .factory('EmployeeShiftOffRequest', EmployeeShiftOffRequest);

    EmployeeShiftOffRequest.$inject = ['$resource'];

    function EmployeeShiftOffRequest ($resource) {
        var resourceUrl =  'api/employee-shift-off-requests/:id';

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
