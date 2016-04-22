(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('PatternDialogController', PatternDialogController);

    PatternDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Pattern'];

    function PatternDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Pattern) {
        var vm = this;
        vm.pattern = entity;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('shiftworkApp:patternUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.pattern.id !== null) {
                Pattern.update(vm.pattern, onSaveSuccess, onSaveError);
            } else {
                Pattern.save(vm.pattern, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
