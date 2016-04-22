(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('ShiftDateController', ShiftDateController);

    ShiftDateController.$inject = ['$scope', '$state', 'ShiftDate'];

    function ShiftDateController ($scope, $state, ShiftDate) {
        var vm = this;
        vm.shiftDates = [];
        vm.loadAll = function() {
            ShiftDate.query(function(result) {
                vm.shiftDates = result;
            });
        };

        vm.loadAll();
        
    }
})();
