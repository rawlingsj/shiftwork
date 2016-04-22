(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('ShiftDeleteController',ShiftDeleteController);

    ShiftDeleteController.$inject = ['$uibModalInstance', 'entity', 'Shift'];

    function ShiftDeleteController($uibModalInstance, entity, Shift) {
        var vm = this;
        vm.shift = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Shift.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
