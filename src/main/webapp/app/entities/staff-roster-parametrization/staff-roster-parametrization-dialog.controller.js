(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('StaffRosterParametrizationDialogController', StaffRosterParametrizationDialogController);

    StaffRosterParametrizationDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'StaffRosterParametrization', 'ShiftDate'];

    function StaffRosterParametrizationDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, StaffRosterParametrization, ShiftDate) {
        var vm = this;
        vm.staffRosterParametrization = entity;
        vm.firstshiftdates = ShiftDate.query({filter: 'staffrosterparametrization-is-null'});
        $q.all([vm.staffRosterParametrization.$promise, vm.firstshiftdates.$promise]).then(function() {
            if (!vm.staffRosterParametrization.firstShiftDate || !vm.staffRosterParametrization.firstShiftDate.id) {
                return $q.reject();
            }
            return ShiftDate.get({id : vm.staffRosterParametrization.firstShiftDate.id}).$promise;
        }).then(function(firstShiftDate) {
            vm.firstshiftdates.push(firstShiftDate);
        });
        vm.lastshiftdates = ShiftDate.query({filter: 'staffrosterparametrization-is-null'});
        $q.all([vm.staffRosterParametrization.$promise, vm.lastshiftdates.$promise]).then(function() {
            if (!vm.staffRosterParametrization.lastShiftDate || !vm.staffRosterParametrization.lastShiftDate.id) {
                return $q.reject();
            }
            return ShiftDate.get({id : vm.staffRosterParametrization.lastShiftDate.id}).$promise;
        }).then(function(lastShiftDate) {
            vm.lastshiftdates.push(lastShiftDate);
        });
        vm.planningwindowstarts = ShiftDate.query({filter: 'staffrosterparametrization-is-null'});
        $q.all([vm.staffRosterParametrization.$promise, vm.planningwindowstarts.$promise]).then(function() {
            if (!vm.staffRosterParametrization.planningWindowStart || !vm.staffRosterParametrization.planningWindowStart.id) {
                return $q.reject();
            }
            return ShiftDate.get({id : vm.staffRosterParametrization.planningWindowStart.id}).$promise;
        }).then(function(planningWindowStart) {
            vm.planningwindowstarts.push(planningWindowStart);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('shiftworkApp:staffRosterParametrizationUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.staffRosterParametrization.id !== null) {
                StaffRosterParametrization.update(vm.staffRosterParametrization, onSaveSuccess, onSaveError);
            } else {
                StaffRosterParametrization.save(vm.staffRosterParametrization, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
