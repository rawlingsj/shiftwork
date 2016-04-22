(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('TaskDialogController', TaskDialogController);

    TaskDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Task', 'Skill'];

    function TaskDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Task, Skill) {
        var vm = this;
        vm.task = entity;
        vm.requiredskills = Skill.query({filter: 'task-is-null'});
        $q.all([vm.task.$promise, vm.requiredskills.$promise]).then(function() {
            if (!vm.task.requiredSkill || !vm.task.requiredSkill.id) {
                return $q.reject();
            }
            return Skill.get({id : vm.task.requiredSkill.id}).$promise;
        }).then(function(requiredSkill) {
            vm.requiredskills.push(requiredSkill);
        });

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
    }
})();
