(function() {
    'use strict';
    angular
        .module('shiftworkApp')
        .factory('EmployeeAbsentReason', EmployeeAbsentReason);

    EmployeeAbsentReason.$inject = ['$resource'];

    function EmployeeAbsentReason ($resource) {
        var resourceUrl =  'api/employee-absent-reasons/:id';

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
