(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('BooleanContractLineDeleteController',BooleanContractLineDeleteController);

    BooleanContractLineDeleteController.$inject = ['$uibModalInstance', 'entity', 'BooleanContractLine'];

    function BooleanContractLineDeleteController($uibModalInstance, entity, BooleanContractLine) {
        var vm = this;
        vm.booleanContractLine = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            BooleanContractLine.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
