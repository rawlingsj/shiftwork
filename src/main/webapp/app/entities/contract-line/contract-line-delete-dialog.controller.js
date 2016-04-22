(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('ContractLineDeleteController',ContractLineDeleteController);

    ContractLineDeleteController.$inject = ['$uibModalInstance', 'entity', 'ContractLine'];

    function ContractLineDeleteController($uibModalInstance, entity, ContractLine) {
        var vm = this;
        vm.contractLine = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            ContractLine.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
