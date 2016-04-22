(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('ShiftAssignmentController', ShiftAssignmentController);

    ShiftAssignmentController.$inject = ['$scope', '$state', 'ShiftAssignment'];

    function ShiftAssignmentController ($scope, $state, ShiftAssignment) {
        var vm = this;
        vm.shiftAssignments = [];
        vm.loadAll = function() {
            ShiftAssignment.query(function(result) {
                vm.shiftAssignments = result;
            });
        };

        vm.loadAll();
        
    }
})();
