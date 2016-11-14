(function() {
    'use strict';
    angular
        .module('shiftworkApp')
        .factory('PlanningJob', PlanningJob);

    PlanningJob.$inject = ['$resource'];

    function PlanningJob ($resource) {
        var resourceUrl =  'api/planning-jobs/:id';

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
