(function() {
    'use strict';
    angular
        .module('shiftworkApp')
        .factory('StaffRosterParametrization', StaffRosterParametrization);

    StaffRosterParametrization.$inject = ['$resource', 'DateUtils'];

    function StaffRosterParametrization ($resource, DateUtils) {
        var resourceUrl =  'api/staff-roster-parametrizations/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.lastRunTime = DateUtils.convertDateTimeFromServer(data.lastRunTime);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
