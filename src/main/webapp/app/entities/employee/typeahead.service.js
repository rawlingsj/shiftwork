(function() {
    'use strict';
    angular
        .module('shiftworkApp')
        .factory('SearchFields', SearchFields);

    SearchFields.$inject = ['$resource'];

    function SearchFields ($resource) {
    	 var resourceUrl =  'api/fields/employees?like=:value';

         return $resource(resourceUrl, {}, {
            'search': {method:'GET',isArray:true}
         });
    }
})();
