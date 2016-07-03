(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('WeekendDefinitionDialogController', WeekendDefinitionDialogController);

    WeekendDefinitionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'WeekendDefinition', 'WeekendDay'];

    function WeekendDefinitionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, WeekendDefinition, WeekendDay) {
        var vm = this;
        vm.weekendDefinition = entity;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('shiftworkApp:weekendDefinitionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.weekendDefinition.id !== null) {
                WeekendDefinition.update(vm.weekendDefinition, onSaveSuccess, onSaveError);
            } else {
                WeekendDefinition.save(vm.weekendDefinition, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
