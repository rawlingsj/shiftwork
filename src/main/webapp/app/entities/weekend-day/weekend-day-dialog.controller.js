(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('WeekendDayDialogController', WeekendDayDialogController);

    WeekendDayDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'WeekendDay', 'WeekendDefinition'];

    function WeekendDayDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, WeekendDay, WeekendDefinition) {
        var vm = this;
        vm.weekendDay = entity;
        vm.weekenddefinitions = WeekendDefinition.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('shiftworkApp:weekendDayUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.weekendDay.id !== null) {
                WeekendDay.update(vm.weekendDay, onSaveSuccess, onSaveError);
            } else {
                WeekendDay.save(vm.weekendDay, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
