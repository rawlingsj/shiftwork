(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('ShiftDialogController', ShiftDialogController);

    ShiftDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Shift', 'ShiftType', 'ShiftDate'];

    function ShiftDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Shift, ShiftType, ShiftDate) {
        var vm = this;
        vm.shift = entity;
        vm.shifttypes = ShiftType.query();
        vm.shiftdates = ShiftDate.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('shiftworkApp:shiftUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.shift.id !== null) {
                Shift.update(vm.shift, onSaveSuccess, onSaveError);
            } else {
                Shift.save(vm.shift, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
