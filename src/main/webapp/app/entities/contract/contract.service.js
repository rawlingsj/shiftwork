(function() {
    'use strict';
    angular
        .module('shiftworkApp')
        .factory('Contract', Contract);

    Contract.$inject = ['$resource'];

    function Contract ($resource) {
        var resourceUrl =  'api/contracts/:id';

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
