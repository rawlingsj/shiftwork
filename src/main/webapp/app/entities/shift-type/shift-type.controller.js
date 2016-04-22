(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('ShiftTypeController', ShiftTypeController);

    ShiftTypeController.$inject = ['$scope', '$state', 'ShiftType'];

    function ShiftTypeController ($scope, $state, ShiftType) {
        var vm = this;
        vm.shiftTypes = [];
        vm.loadAll = function() {
            ShiftType.query(function(result) {
                vm.shiftTypes = result;
            });
        };

        vm.loadAll();
        
    }
})();
