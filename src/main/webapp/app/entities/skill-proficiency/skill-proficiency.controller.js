(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('SkillProficiencyController', SkillProficiencyController);

    SkillProficiencyController.$inject = ['$scope', '$state', 'SkillProficiency'];

    function SkillProficiencyController ($scope, $state, SkillProficiency) {
        var vm = this;
        vm.skillProficiencies = [];
        vm.loadAll = function() {
            SkillProficiency.query(function(result) {
                vm.skillProficiencies = result;
            });
        };

        vm.loadAll();
        
    }
})();
