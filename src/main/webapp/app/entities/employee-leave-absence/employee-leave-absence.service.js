(function() {
    'use strict';
    angular
        .module('shiftworkApp')
        .factory('EmployeeLeaveAbsence', EmployeeLeaveAbsence);

    EmployeeLeaveAbsence.$inject = ['$resource', 'DateUtils'];

    function EmployeeLeaveAbsence ($resource, DateUtils) {
        var resourceUrl =  'api/employee-leave-absences/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.absentFrom = DateUtils.convertDateTimeFromServer(data.absentFrom);
                    data.absentTo = DateUtils.convertDateTimeFromServer(data.absentTo);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
