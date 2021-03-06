(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('PlanningJobController', PlanningJobController);

    PlanningJobController.$inject = ['$scope', '$state', 'PlanningJob', 'StaffRoster'];

    function PlanningJobController ($scope, $state, PlanningJob, StaffRoster) {
        var vm = this;
        vm.planningJobs = [];
        vm.loadAll = function() {
            PlanningJob.query(function(result) {
                vm.planningJobs = result;
            });
        };

        vm.loadAll();

        vm.refresh = function(jobId) {
            PlanningJob.update({id: jobId}, null, vm.loadAll);
        };

        vm.refreshAll = function() {
            PlanningJob.update(null, vm.loadAll);
        };

        vm.save = function(jobId) {
            PlanningJob.get({id: jobId}, function(result) {
                var staffRoster = {
                    'staffRosterParametrization': result.parameterization,
                    'shiftAssignments': result.shiftAssignments
                };
                StaffRoster.save(staffRoster);
            });
        };
    }
})();
