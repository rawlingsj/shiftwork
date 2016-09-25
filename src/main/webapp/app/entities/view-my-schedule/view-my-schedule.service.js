(function () {
    'use strict';
	
    angular
        .module('shiftworkApp')
        .factory('ViewMySchedule', ViewMySchedule);

    ViewMySchedule.$inject = ['$resource', 'DateUtils'];

    function ViewMySchedule ($resource, DateUtils) {
        var resourceUrl =  'api/schedules/:id';

		return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
			}
        });
    }
})();
