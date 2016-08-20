(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('PlanningJobController', PlanningJobController);

    PlanningJobController.$inject = ['$scope', '$state', 'PlanningJob'];

    function PlanningJobController ($scope, $state, PlanningJob) {
        var vm = this;
        vm.planningJobs = [];
        vm.loadAll = function() {
            PlanningJob.query(function(result) {
                vm.planningJobs = result;
            });
        };

        vm.loadAll();

        vm.refresh = function(jobId) {
            PlanningJob.update({id: jobId}, null);
            vm.loadAll();
        };
    }
})();
