(function() {
    'use strict';
    angular
        .module('shiftworkApp')
        .factory('TaskSkillRequirement', TaskSkillRequirement);

    TaskSkillRequirement.$inject = ['$resource'];

    function TaskSkillRequirement ($resource) {
        var resourceUrl =  'api/task-skill-requirements/:id';

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
