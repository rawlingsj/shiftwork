(function () {
    'use strict';
    angular
        .module('shiftworkApp')
        .factory('Typeahead', Typeahead);

    Typeahead.$inject = ['$resource'];

    function Typeahead($resource) {
        return {
            findEmployees: findEmployees
        };

        function findEmployees(value) {
            return $resource('api/fields/employees?like=:like', {like: value}).query().$promise;
        }
    }
})();
