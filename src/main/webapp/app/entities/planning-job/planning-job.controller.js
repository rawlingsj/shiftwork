(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('PlanningJobController', PlanningJobController);

    PlanningJobController.$inject = ['$scope', '$state', 'PlanningJob', 'StaffRoster', 'WS'];

    function PlanningJobController ($scope, $state, PlanningJob, StaffRoster, WS) {
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

        vm.connect = function () {
            WS.connect();
        }

        vm.disconnect = function () {
            WS.disconnect();
        }

        vm.update = function () {
            WS.get();
        }

        $scope.$on('score', listenScore);

        function listenScore($event, jobStatusUpdate) {
            $("#score" + jobStatusUpdate.jobId).text(jobStatusUpdate.hardConstraintMatches +
                '/' + jobStatusUpdate.softConstraintMatches);
            $("#status" + jobStatusUpdate.jobId).text(jobStatusUpdate.status);
        }
    }
})();
