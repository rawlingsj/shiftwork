(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('ShiftController', ShiftController);

    ShiftController.$inject = ['$scope', '$state', 'Shift'];

    function ShiftController ($scope, $state, Shift) {
        var vm = this;
        vm.shifts = [];
        vm.loadAll = function() {
            Shift.query(function(result) {
                vm.shifts = result;
            });
        };

        vm.loadAll();
        
    }
})();
