(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('ShiftAssignmentDeleteController',ShiftAssignmentDeleteController);

    ShiftAssignmentDeleteController.$inject = ['$uibModalInstance', 'entity', 'ShiftAssignment'];

    function ShiftAssignmentDeleteController($uibModalInstance, entity, ShiftAssignment) {
        var vm = this;
        vm.shiftAssignment = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            ShiftAssignment.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
