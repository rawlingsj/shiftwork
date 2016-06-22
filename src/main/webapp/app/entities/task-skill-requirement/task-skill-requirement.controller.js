(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('TaskSkillRequirementController', TaskSkillRequirementController);

    TaskSkillRequirementController.$inject = ['$scope', '$state', 'TaskSkillRequirement'];

    function TaskSkillRequirementController ($scope, $state, TaskSkillRequirement) {
        var vm = this;
        vm.taskSkillRequirements = [];
        vm.loadAll = function() {
            TaskSkillRequirement.query(function(result) {
                vm.taskSkillRequirements = result;
            });
        };

        vm.loadAll();
        
    }
})();
