(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('SkillProficiencyDeleteController',SkillProficiencyDeleteController);

    SkillProficiencyDeleteController.$inject = ['$uibModalInstance', 'entity', 'SkillProficiency'];

    function SkillProficiencyDeleteController($uibModalInstance, entity, SkillProficiency) {
        var vm = this;
        vm.skillProficiency = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            SkillProficiency.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
