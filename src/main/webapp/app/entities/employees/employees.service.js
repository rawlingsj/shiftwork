(function() {
    'use strict';
    angular
        .module('shiftworkApp')
        .factory('Employees', Employees);

    Employees.$inject = ['$resource'];

    function Employees ($resource) {
        var resourceUrl =  'api/employees2/:id';

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
