(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('EmployeeShiftOffRequestDialogController', EmployeeShiftOffRequestDialogController);

    EmployeeShiftOffRequestDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'EmployeeShiftOffRequest', 'Shift', 'Employee'];

    function EmployeeShiftOffRequestDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, EmployeeShiftOffRequest, Shift, Employee) {
        var vm = this;
        vm.employeeShiftOffRequest = entity;
        vm.shifts = Shift.query();
        vm.employees = Employee.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('shiftworkApp:employeeShiftOffRequestUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.employeeShiftOffRequest.id !== null) {
                EmployeeShiftOffRequest.update(vm.employeeShiftOffRequest, onSaveSuccess, onSaveError);
            } else {
                EmployeeShiftOffRequest.save(vm.employeeShiftOffRequest, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
