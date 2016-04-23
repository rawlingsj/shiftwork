(function() {
    'use strict';
    angular
        .module('shiftworkApp')
        .factory('MinMaxContractLine', MinMaxContractLine);

    MinMaxContractLine.$inject = ['$resource'];

    function MinMaxContractLine ($resource) {
        var resourceUrl =  'api/min-max-contract-lines/:id';

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
