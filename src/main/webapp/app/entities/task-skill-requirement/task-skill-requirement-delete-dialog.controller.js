(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('TaskSkillRequirementDeleteController',TaskSkillRequirementDeleteController);

    TaskSkillRequirementDeleteController.$inject = ['$uibModalInstance', 'entity', 'TaskSkillRequirement'];

    function TaskSkillRequirementDeleteController($uibModalInstance, entity, TaskSkillRequirement) {
        var vm = this;
        vm.taskSkillRequirement = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            TaskSkillRequirement.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
