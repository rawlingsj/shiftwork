(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('ContractDeleteController',ContractDeleteController);

    ContractDeleteController.$inject = ['$uibModalInstance', 'entity', 'Contract'];

    function ContractDeleteController($uibModalInstance, entity, Contract) {
        var vm = this;
        vm.contract = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Contract.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
