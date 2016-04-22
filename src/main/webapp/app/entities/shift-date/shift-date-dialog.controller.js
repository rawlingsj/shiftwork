(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('ShiftDateDialogController', ShiftDateDialogController);

    ShiftDateDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ShiftDate', 'Shift'];

    function ShiftDateDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ShiftDate, Shift) {
        var vm = this;
        vm.shiftDate = entity;
        vm.shifts = Shift.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('shiftworkApp:shiftDateUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.shiftDate.id !== null) {
                ShiftDate.update(vm.shiftDate, onSaveSuccess, onSaveError);
            } else {
                ShiftDate.save(vm.shiftDate, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
