(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('TaskDialogController', TaskDialogController);

    TaskDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Task'];

    function TaskDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Task) {
        var vm = this;
        vm.task = entity;
        vm.duplicateMsg = false;
        vm.editId = $stateParams.id === null ? 0 : parseInt($stateParams.id);
        vm.tasks = [];
        vm.loadAll = function() {
            Task.query(function(result) {
                vm.tasks = result;
            });
        };

        vm.loadAll();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('shiftworkApp:taskUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.task.id !== null) {
                Task.update(vm.task, onSaveSuccess, onSaveError);
            } else {
                Task.save(vm.task, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.verifyDuplicate = function(code) {
            vm.duplicateMsg = false;
            angular.forEach(vm.tasks, function(task, key){
                if(task.code === code) {
                    if( vm.editId === 0) {
                        console.log('existed');
                        vm.duplicateMsg = true;
                        return vm.duplicateMsg;
                    }
                    else if(vm.editId != task.id) {
                        console.log('existed');
                        vm.duplicateMsg = true;
                        return vm.duplicateMsg;                       
                    }
                }
            });
            return vm.duplicateMsg;
        }
    }
})();
