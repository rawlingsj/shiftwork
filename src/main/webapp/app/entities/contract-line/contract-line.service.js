(function() {
    'use strict';
    angular
        .module('shiftworkApp')
        .factory('ContractLine', ContractLine);

    ContractLine.$inject = ['$resource'];

    function ContractLine ($resource) {
        var resourceUrl =  'api/contract-lines/:id';

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
