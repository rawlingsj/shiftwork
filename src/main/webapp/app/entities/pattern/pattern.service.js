(function() {
    'use strict';
    angular
        .module('shiftworkApp')
        .factory('Pattern', Pattern);

    Pattern.$inject = ['$resource'];

    function Pattern ($resource) {
        var resourceUrl =  'api/patterns/:id';

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
