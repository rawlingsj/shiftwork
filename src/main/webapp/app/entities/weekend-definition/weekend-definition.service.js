(function() {
    'use strict';
    angular
        .module('shiftworkApp')
        .factory('WeekendDefinition', WeekendDefinition);

    WeekendDefinition.$inject = ['$resource'];

    function WeekendDefinition ($resource) {
        var resourceUrl =  'api/weekend-definitions/:id';

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
