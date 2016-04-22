(function() {
    'use strict';
    angular
        .module('shiftworkApp')
        .factory('StaffRosterParametrization', StaffRosterParametrization);

    StaffRosterParametrization.$inject = ['$resource'];

    function StaffRosterParametrization ($resource) {
        var resourceUrl =  'api/staff-roster-parametrizations/:id';

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
