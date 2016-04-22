(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('StaffRosterParametrizationController', StaffRosterParametrizationController);

    StaffRosterParametrizationController.$inject = ['$scope', '$state', 'StaffRosterParametrization'];

    function StaffRosterParametrizationController ($scope, $state, StaffRosterParametrization) {
        var vm = this;
        vm.staffRosterParametrizations = [];
        vm.loadAll = function() {
            StaffRosterParametrization.query(function(result) {
                vm.staffRosterParametrizations = result;
            });
        };

        vm.loadAll();
        
    }
})();
