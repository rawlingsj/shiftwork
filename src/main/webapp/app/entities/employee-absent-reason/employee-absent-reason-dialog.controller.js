(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('EmployeeAbsentReasonDialogController', EmployeeAbsentReasonDialogController);

    EmployeeAbsentReasonDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'EmployeeAbsentReason'];

    function EmployeeAbsentReasonDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, EmployeeAbsentReason) {
        var vm = this;
        vm.employeeAbsentReason = entity;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('shiftworkApp:employeeAbsentReasonUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.employeeAbsentReason.id !== null) {
                EmployeeAbsentReason.update(vm.employeeAbsentReason, onSaveSuccess, onSaveError);
            } else {
                EmployeeAbsentReason.save(vm.employeeAbsentReason, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
