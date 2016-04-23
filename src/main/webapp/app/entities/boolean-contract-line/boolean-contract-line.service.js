(function() {
    'use strict';
    angular
        .module('shiftworkApp')
        .factory('BooleanContractLine', BooleanContractLine);

    BooleanContractLine.$inject = ['$resource'];

    function BooleanContractLine ($resource) {
        var resourceUrl =  'api/boolean-contract-lines/:id';

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
