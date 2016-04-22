(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('ContractLineDialogController', ContractLineDialogController);

    ContractLineDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ContractLine', 'Contract'];

    function ContractLineDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ContractLine, Contract) {
        var vm = this;
        vm.contractLine = entity;
        vm.contracts = Contract.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('shiftworkApp:contractLineUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.contractLine.id !== null) {
                ContractLine.update(vm.contractLine, onSaveSuccess, onSaveError);
            } else {
                ContractLine.save(vm.contractLine, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
