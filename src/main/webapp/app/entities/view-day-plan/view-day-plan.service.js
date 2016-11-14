(function() {
    'use strict';
    angular
        .module('shiftworkApp')
        .factory('ViewDayPlan', ViewDayPlan);

    ViewDayPlan.$inject = ['$resource', 'DateUtils'];

    function ViewDayPlan ($resource, DateUtils) {
        var resourceUrl =  'api/schedules';

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
