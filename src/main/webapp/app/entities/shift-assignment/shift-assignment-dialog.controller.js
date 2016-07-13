(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('ShiftAssignmentDialogController', ShiftAssignmentDialogController);

    ShiftAssignmentDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ShiftAssignment', 'Shift', 'Employee', 'Task'];

    function ShiftAssignmentDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ShiftAssignment, Shift, Employee, Task) {
        var vm = this;
        vm.shiftAssignment = entity;
        vm.shifts = Shift.query();
        vm.employees = Employee.query();
        vm.tasks = Task.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('shiftworkApp:shiftAssignmentUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.shiftAssignment.id !== null) {
                ShiftAssignment.update(vm.shiftAssignment, onSaveSuccess, onSaveError);
            } else {
                ShiftAssignment.save(vm.shiftAssignment, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
