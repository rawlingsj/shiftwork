(function() {
    'use strict';
    angular
        .module('shiftworkApp')
        .factory('ViewMySchedule', ViewMySchedule);

    ViewMySchedule.$inject = ['$resource', 'DateUtils'];

    function ViewMySchedule ($resource, DateUtils) {
        var resourceUrl =  'api/shift-assignments/:id';

        //return $resource(resourceUrl, { shiftDate : 'shiftDate' }, {
		return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.date = DateUtils.convertLocalDateFromServer(data.date);
                    return data;
                }
			}
        });
    }
})();
