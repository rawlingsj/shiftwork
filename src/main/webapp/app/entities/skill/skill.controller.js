(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('SkillController', SkillController);

    SkillController.$inject = ['$scope', '$state', 'Skill'];

    function SkillController ($scope, $state, Skill) {
        var vm = this;
        vm.skills = [];
        vm.loadAll = function() {
            Skill.query(function(result) {
                vm.skills = result;
            });
        };

        vm.loadAll();
        
    }
})();
