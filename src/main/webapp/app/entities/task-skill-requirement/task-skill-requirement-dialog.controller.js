(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('TaskSkillRequirementDialogController', TaskSkillRequirementDialogController);

    TaskSkillRequirementDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'TaskSkillRequirement', 'Task', 'Skill'];

    function TaskSkillRequirementDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, TaskSkillRequirement, Task, Skill) {
        var vm = this;
        vm.taskSkillRequirement = entity;
        vm.tasks = Task.query({filter: 'taskskillrequirement-is-null'});
        $q.all([vm.taskSkillRequirement.$promise, vm.tasks.$promise]).then(function() {
            if (!vm.taskSkillRequirement.task || !vm.taskSkillRequirement.task.id) {
                return $q.reject();
            }
            return Task.get({id : vm.taskSkillRequirement.task.id}).$promise;
        }).then(function(task) {
            vm.tasks.push(task);
        });
        vm.skills = Skill.query({filter: 'taskskillrequirement-is-null'});
        $q.all([vm.taskSkillRequirement.$promise, vm.skills.$promise]).then(function() {
            if (!vm.taskSkillRequirement.skill || !vm.taskSkillRequirement.skill.id) {
                return $q.reject();
            }
            return Skill.get({id : vm.taskSkillRequirement.skill.id}).$promise;
        }).then(function(skill) {
            vm.skills.push(skill);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('shiftworkApp:taskSkillRequirementUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.taskSkillRequirement.id !== null) {
                TaskSkillRequirement.update(vm.taskSkillRequirement, onSaveSuccess, onSaveError);
            } else {
                TaskSkillRequirement.save(vm.taskSkillRequirement, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
