(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('SkillDetailController', SkillDetailController);

    SkillDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Skill', 'SkillProficiency'];

    function SkillDetailController($scope, $rootScope, $stateParams, entity, Skill, SkillProficiency) {
        var vm = this;
        vm.skill = entity;
        
        var unsubscribe = $rootScope.$on('shiftworkApp:skillUpdate', function(event, result) {
            vm.skill = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
