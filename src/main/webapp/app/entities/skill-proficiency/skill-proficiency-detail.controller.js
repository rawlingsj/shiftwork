(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('SkillProficiencyDetailController', SkillProficiencyDetailController);

    SkillProficiencyDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'SkillProficiency', 'Skill', 'Employee'];

    function SkillProficiencyDetailController($scope, $rootScope, $stateParams, entity, SkillProficiency, Skill, Employee) {
        var vm = this;
        vm.skillProficiency = entity;
        
        var unsubscribe = $rootScope.$on('shiftworkApp:skillProficiencyUpdate', function(event, result) {
            vm.skillProficiency = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
