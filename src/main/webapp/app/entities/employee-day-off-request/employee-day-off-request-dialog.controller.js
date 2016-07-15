(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('EmployeeDayOffRequestDialogController', EmployeeDayOffRequestDialogController);

    EmployeeDayOffRequestDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'EmployeeDayOffRequest', 'ShiftDate', 'Employee'];

    function EmployeeDayOffRequestDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, EmployeeDayOffRequest, ShiftDate, Employee) {
        var vm = this;
        vm.employeeDayOffRequest = entity;
        vm.shiftdates = ShiftDate.query();
        vm.employees = Employee.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('shiftworkApp:employeeDayOffRequestUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.employeeDayOffRequest.id !== null) {
                EmployeeDayOffRequest.update(vm.employeeDayOffRequest, onSaveSuccess, onSaveError);
            } else {
                EmployeeDayOffRequest.save(vm.employeeDayOffRequest, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
