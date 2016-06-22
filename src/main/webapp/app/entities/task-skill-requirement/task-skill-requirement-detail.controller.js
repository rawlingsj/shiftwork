(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('TaskSkillRequirementDetailController', TaskSkillRequirementDetailController);

    TaskSkillRequirementDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'TaskSkillRequirement', 'Task', 'Skill'];

    function TaskSkillRequirementDetailController($scope, $rootScope, $stateParams, entity, TaskSkillRequirement, Task, Skill) {
        var vm = this;
        vm.taskSkillRequirement = entity;
        
        var unsubscribe = $rootScope.$on('shiftworkApp:taskSkillRequirementUpdate', function(event, result) {
            vm.taskSkillRequirement = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
