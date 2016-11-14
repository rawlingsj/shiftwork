(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('PlanningJobDialogController', PlanningJobDialogController);

    PlanningJobDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'PlanningJob', 'StaffRosterParametrization'];

    function PlanningJobDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, PlanningJob, StaffRosterParametrization) {
        var vm = this;
        vm.planningJob = entity;
        vm.parameterizations = StaffRosterParametrization.query({filter: 'planningjob-is-null'});
        $q.all([vm.planningJob.$promise, vm.parameterizations.$promise]).then(function() {
            if (!vm.planningJob.parameterization || !vm.planningJob.parameterization.id) {
                return $q.reject();
            }
            return StaffRosterParametrization.get({id : vm.planningJob.parameterization.id}).$promise;
        }).then(function(parameterization) {
            vm.parameterizations.push(parameterization);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('shiftworkApp:planningJobUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.planningJob.id !== null) {
                PlanningJob.update(vm.planningJob, onSaveSuccess, onSaveError);
            } else {
                PlanningJob.save(vm.planningJob, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
