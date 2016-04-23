(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('MinMaxContractLineDialogController', MinMaxContractLineDialogController);

    MinMaxContractLineDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'MinMaxContractLine', 'Contract'];

    function MinMaxContractLineDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, MinMaxContractLine, Contract) {
        var vm = this;
        vm.minMaxContractLine = entity;
        vm.contracts = Contract.query({filter: 'minmaxcontractline-is-null'});
        $q.all([vm.minMaxContractLine.$promise, vm.contracts.$promise]).then(function() {
            if (!vm.minMaxContractLine.contract || !vm.minMaxContractLine.contract.id) {
                return $q.reject();
            }
            return Contract.get({id : vm.minMaxContractLine.contract.id}).$promise;
        }).then(function(contract) {
            vm.contracts.push(contract);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('shiftworkApp:minMaxContractLineUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.minMaxContractLine.id !== null) {
                MinMaxContractLine.update(vm.minMaxContractLine, onSaveSuccess, onSaveError);
            } else {
                MinMaxContractLine.save(vm.minMaxContractLine, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
