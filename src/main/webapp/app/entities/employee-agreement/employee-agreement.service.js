(function() {
    'use strict';
    angular
        .module('shiftworkApp')
        .factory('EmployeeAgreement', EmployeeAgreement);

    EmployeeAgreement.$inject = ['$resource'];

    function EmployeeAgreement ($resource) {
        var resourceUrl =  'api/contracts/:id';

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
