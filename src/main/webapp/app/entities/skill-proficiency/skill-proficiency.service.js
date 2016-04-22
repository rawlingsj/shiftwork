(function() {
    'use strict';
    angular
        .module('shiftworkApp')
        .factory('SkillProficiency', SkillProficiency);

    SkillProficiency.$inject = ['$resource'];

    function SkillProficiency ($resource) {
        var resourceUrl =  'api/skill-proficiencies/:id';

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
