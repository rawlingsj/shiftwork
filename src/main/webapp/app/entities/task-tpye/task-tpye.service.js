(function() {
    'use strict';
    angular
        .module('shiftworkApp')
        .factory('TaskTpye', TaskTpye);

    TaskTpye.$inject = ['$resource'];

    function TaskTpye ($resource) {
        var resourceUrl =  'api/task-tpyes/:id';

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
