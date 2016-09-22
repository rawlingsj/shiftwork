(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('TaskDialogController', TaskDialogController);

    TaskDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Task'];

    function TaskDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Task) {
        var vm = this;
        vm.task = entity;
        $scope.duplicateMsg = false;
        $scope.tasks = [];
        vm.loadAll = function() {
            Task.query(function(result) {
                $scope.tasks = result;
                console.log('len1: ' + $scope.tasks.length);
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
            $scope.duplicateMsg = false;
            angular.forEach($scope.tasks, function(task, key){
                 console.log(key + ': ' + task.code);
                 if(task.code === code) {
                    console.log('existed');
                    $scope.duplicateMsg = true;
                    return true;
                 }
            });
        }
    }
})();
