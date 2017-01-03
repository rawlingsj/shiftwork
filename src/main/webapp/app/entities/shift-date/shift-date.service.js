(function() {
    'use strict';
    angular
        .module('shiftworkApp')
        .factory('ShiftDate', ShiftDate);

    ShiftDate.$inject = ['$resource', 'DateUtils'];

    function ShiftDate ($resource, DateUtils) {
        var resourceUrl =  'api/shift-dates/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.date = DateUtils.convertLocalDateFromServer(data.date);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.date = DateUtils.convertLocalDateToServer(data.date);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                isArray: true,
                transformRequest: function (data) {
                    data.date = DateUtils.convertLocalDateToServer(data.date);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
