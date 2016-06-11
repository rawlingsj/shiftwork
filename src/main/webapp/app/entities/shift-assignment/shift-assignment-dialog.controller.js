(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('ShiftAssignmentDialogController', ShiftAssignmentDialogController);

    ShiftAssignmentDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'ShiftAssignment', 'Shift', 'Employee', 'Task'];

    function ShiftAssignmentDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, ShiftAssignment, Shift, Employee, Task) {
        var vm = this;
        vm.shiftAssignment = entity;
        vm.shifts = Shift.query({filter: 'shiftassignment-is-null'});
        $q.all([vm.shiftAssignment.$promise, vm.shifts.$promise]).then(function() {
            if (!vm.shiftAssignment.shift || !vm.shiftAssignment.shift.id) {
                return $q.reject();
            }
            return Shift.get({id : vm.shiftAssignment.shift.id}).$promise;
        }).then(function(shift) {
            vm.shifts.push(shift);
        });
        vm.employees = Employee.query({filter: 'shiftassignment-is-null'});
        $q.all([vm.shiftAssignment.$promise, vm.employees.$promise]).then(function() {
            if (!vm.shiftAssignment.employee || !vm.shiftAssignment.employee.id) {
                return $q.reject();
            }
            return Employee.get({id : vm.shiftAssignment.employee.id}).$promise;
        }).then(function(employee) {
            vm.employees.push(employee);
        });
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
