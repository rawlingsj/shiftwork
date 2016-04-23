(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('MinMaxContractLineDeleteController',MinMaxContractLineDeleteController);

    MinMaxContractLineDeleteController.$inject = ['$uibModalInstance', 'entity', 'MinMaxContractLine'];

    function MinMaxContractLineDeleteController($uibModalInstance, entity, MinMaxContractLine) {
        var vm = this;
        vm.minMaxContractLine = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            MinMaxContractLine.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
