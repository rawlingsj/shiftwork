(function() {
    'use strict';
    angular
        .module('shiftworkApp')
        .factory('PatternContractLine', PatternContractLine);

    PatternContractLine.$inject = ['$resource'];

    function PatternContractLine ($resource) {
        var resourceUrl =  'api/pattern-contract-lines/:id';

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
