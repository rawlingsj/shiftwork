(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('PatternContractLineDeleteController',PatternContractLineDeleteController);

    PatternContractLineDeleteController.$inject = ['$uibModalInstance', 'entity', 'PatternContractLine'];

    function PatternContractLineDeleteController($uibModalInstance, entity, PatternContractLine) {
        var vm = this;
        vm.patternContractLine = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            PatternContractLine.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
