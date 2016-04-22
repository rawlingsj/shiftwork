(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('ShiftTypeDeleteController',ShiftTypeDeleteController);

    ShiftTypeDeleteController.$inject = ['$uibModalInstance', 'entity', 'ShiftType'];

    function ShiftTypeDeleteController($uibModalInstance, entity, ShiftType) {
        var vm = this;
        vm.shiftType = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            ShiftType.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
