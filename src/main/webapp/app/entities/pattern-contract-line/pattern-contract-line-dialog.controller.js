(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('PatternContractLineDialogController', PatternContractLineDialogController);

    PatternContractLineDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'PatternContractLine', 'Contract', 'Pattern'];

    function PatternContractLineDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, PatternContractLine, Contract, Pattern) {
        var vm = this;
        vm.patternContractLine = entity;
        vm.contracts = Contract.query({filter: 'patterncontractline-is-null'});
        $q.all([vm.patternContractLine.$promise, vm.contracts.$promise]).then(function() {
            if (!vm.patternContractLine.contract || !vm.patternContractLine.contract.id) {
                return $q.reject();
            }
            return Contract.get({id : vm.patternContractLine.contract.id}).$promise;
        }).then(function(contract) {
            vm.contracts.push(contract);
        });
        vm.patterns = Pattern.query({filter: 'patterncontractline-is-null'});
        $q.all([vm.patternContractLine.$promise, vm.patterns.$promise]).then(function() {
            if (!vm.patternContractLine.pattern || !vm.patternContractLine.pattern.id) {
                return $q.reject();
            }
            return Pattern.get({id : vm.patternContractLine.pattern.id}).$promise;
        }).then(function(pattern) {
            vm.patterns.push(pattern);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('shiftworkApp:patternContractLineUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.patternContractLine.id !== null) {
                PatternContractLine.update(vm.patternContractLine, onSaveSuccess, onSaveError);
            } else {
                PatternContractLine.save(vm.patternContractLine, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
