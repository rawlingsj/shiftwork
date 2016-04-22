(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('TaskTpyeDialogController', TaskTpyeDialogController);

    TaskTpyeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'TaskTpye'];

    function TaskTpyeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, TaskTpye) {
        var vm = this;
        vm.taskTpye = entity;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('shiftworkApp:taskTpyeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.taskTpye.id !== null) {
                TaskTpye.update(vm.taskTpye, onSaveSuccess, onSaveError);
            } else {
                TaskTpye.save(vm.taskTpye, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
