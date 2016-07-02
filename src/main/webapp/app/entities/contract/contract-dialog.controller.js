(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('ContractDialogController', ContractDialogController);

    ContractDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Contract', 'ContractLine', 'WeekendDefinition'];

    function ContractDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Contract, ContractLine, WeekendDefinition) {
        var vm = this;
        vm.contract = entity;
        vm.contractlines = ContractLine.query();
        vm.weekenddefinitions = WeekendDefinition.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('shiftworkApp:contractUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.contract.id !== null) {
                Contract.update(vm.contract, onSaveSuccess, onSaveError);
            } else {
                Contract.save(vm.contract, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
