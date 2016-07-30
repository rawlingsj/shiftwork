(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('ShiftTypeDialogController', ShiftTypeDialogController);

    ShiftTypeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ShiftType', 'Task'];

    function ShiftTypeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ShiftType, Task) {
        var vm = this;
        vm.shiftType = entity;
        vm.tasks = Task.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('shiftworkApp:shiftTypeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.shiftType.id !== null) {
                ShiftType.update(vm.shiftType, onSaveSuccess, onSaveError);
            } else {
                ShiftType.save(vm.shiftType, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
