(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('ShiftDateDeleteController',ShiftDateDeleteController);

    ShiftDateDeleteController.$inject = ['$uibModalInstance', 'entity', 'ShiftDate'];

    function ShiftDateDeleteController($uibModalInstance, entity, ShiftDate) {
        var vm = this;
        vm.shiftDate = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            ShiftDate.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
