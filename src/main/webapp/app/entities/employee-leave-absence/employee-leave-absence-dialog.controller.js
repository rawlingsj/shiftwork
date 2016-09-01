(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('EmployeeLeaveAbsenceDialogController', EmployeeLeaveAbsenceDialogController);

    EmployeeLeaveAbsenceDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'EmployeeLeaveAbsence', 'EmployeeAbsentReason', 'Employee'];

    function EmployeeLeaveAbsenceDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, EmployeeLeaveAbsence, EmployeeAbsentReason, Employee) {
        var vm = this;
        vm.employeeLeaveAbsence = entity;
        vm.employeeabsentreasons = EmployeeAbsentReason.query();
        vm.employees = Employee.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('shiftworkApp:employeeLeaveAbsenceUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.employeeLeaveAbsence.id !== null) {
                EmployeeLeaveAbsence.update(vm.employeeLeaveAbsence, onSaveSuccess, onSaveError);
            } else {
                EmployeeLeaveAbsence.save(vm.employeeLeaveAbsence, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.absentFrom = false;
        vm.datePickerOpenStatus.absentTo = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
