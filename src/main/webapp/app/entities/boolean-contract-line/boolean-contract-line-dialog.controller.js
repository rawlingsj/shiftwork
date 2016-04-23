(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('BooleanContractLineDialogController', BooleanContractLineDialogController);

    BooleanContractLineDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'BooleanContractLine', 'Contract'];

    function BooleanContractLineDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, BooleanContractLine, Contract) {
        var vm = this;
        vm.booleanContractLine = entity;
        vm.contracts = Contract.query({filter: 'booleancontractline-is-null'});
        $q.all([vm.booleanContractLine.$promise, vm.contracts.$promise]).then(function() {
            if (!vm.booleanContractLine.contract || !vm.booleanContractLine.contract.id) {
                return $q.reject();
            }
            return Contract.get({id : vm.booleanContractLine.contract.id}).$promise;
        }).then(function(contract) {
            vm.contracts.push(contract);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('shiftworkApp:booleanContractLineUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.booleanContractLine.id !== null) {
                BooleanContractLine.update(vm.booleanContractLine, onSaveSuccess, onSaveError);
            } else {
                BooleanContractLine.save(vm.booleanContractLine, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
