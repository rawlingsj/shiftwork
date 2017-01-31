(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('StaffRosterParametrizationDialogController', StaffRosterParametrizationDialogController);

    StaffRosterParametrizationDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'StaffRosterParametrization', 'ShiftDate'];

    function StaffRosterParametrizationDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, StaffRosterParametrization, ShiftDate) {
        var vm = this;
        vm.staffRosterParametrization = entity;
        vm.shiftdates = ShiftDate.query();

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

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.lastRunTime = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
