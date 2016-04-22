(function() {
    'use strict';
    angular
        .module('shiftworkApp')
        .factory('WeekendDay', WeekendDay);

    WeekendDay.$inject = ['$resource'];

    function WeekendDay ($resource) {
        var resourceUrl =  'api/weekend-days/:id';

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
