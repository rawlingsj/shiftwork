(function() {
    'use strict';
    angular
        .module('shiftworkApp')
        .factory('StaffRoster', StaffRoster);

    StaffRoster.$inject = ['$resource'];

    function StaffRoster ($resource) {
        var resourceUrl =  'api/staff-rosters';

        return $resource(resourceUrl, {}, {
            'save':   {method:'POST'}
        });
    }
})();
