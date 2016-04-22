(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('ContractDialogController', ContractDialogController);

    ContractDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Contract', 'WeekendDefinition', 'ContractLine'];

    function ContractDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Contract, WeekendDefinition, ContractLine) {
        var vm = this;
        vm.contract = entity;
        vm.weekenddefinitions = WeekendDefinition.query({filter: 'contract-is-null'});
        $q.all([vm.contract.$promise, vm.weekenddefinitions.$promise]).then(function() {
            if (!vm.contract.weekendDefinition || !vm.contract.weekendDefinition.id) {
                return $q.reject();
            }
            return WeekendDefinition.get({id : vm.contract.weekendDefinition.id}).$promise;
        }).then(function(weekendDefinition) {
            vm.weekenddefinitions.push(weekendDefinition);
        });
        vm.contractlines = ContractLine.query();

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
