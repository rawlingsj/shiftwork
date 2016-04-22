(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('ShiftTypeTaskDialogController', ShiftTypeTaskDialogController);

    ShiftTypeTaskDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'ShiftTypeTask', 'Task', 'ShiftType'];

    function ShiftTypeTaskDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, ShiftTypeTask, Task, ShiftType) {
        var vm = this;
        vm.shiftTypeTask = entity;
        vm.tasks = Task.query({filter: 'shifttypetask-is-null'});
        $q.all([vm.shiftTypeTask.$promise, vm.tasks.$promise]).then(function() {
            if (!vm.shiftTypeTask.task || !vm.shiftTypeTask.task.id) {
                return $q.reject();
            }
            return Task.get({id : vm.shiftTypeTask.task.id}).$promise;
        }).then(function(task) {
            vm.tasks.push(task);
        });
        vm.shifttypes = ShiftType.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('shiftworkApp:shiftTypeTaskUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.shiftTypeTask.id !== null) {
                ShiftTypeTask.update(vm.shiftTypeTask, onSaveSuccess, onSaveError);
            } else {
                ShiftTypeTask.save(vm.shiftTypeTask, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
