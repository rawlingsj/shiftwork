(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('PlanningJobDeleteController',PlanningJobDeleteController);

    PlanningJobDeleteController.$inject = ['$uibModalInstance', 'entity', 'PlanningJob'];

    function PlanningJobDeleteController($uibModalInstance, entity, PlanningJob) {
        var vm = this;
        vm.planningJob = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            PlanningJob.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
