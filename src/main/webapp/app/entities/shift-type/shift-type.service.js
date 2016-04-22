(function() {
    'use strict';
    angular
        .module('shiftworkApp')
        .factory('ShiftType', ShiftType);

    ShiftType.$inject = ['$resource', 'DateUtils'];

    function ShiftType ($resource, DateUtils) {
        var resourceUrl =  'api/shift-types/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.startTime = DateUtils.convertDateTimeFromServer(data.startTime);
                    data.endTime = DateUtils.convertDateTimeFromServer(data.endTime);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
