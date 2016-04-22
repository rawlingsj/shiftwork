(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('ShiftTypeTaskDeleteController',ShiftTypeTaskDeleteController);

    ShiftTypeTaskDeleteController.$inject = ['$uibModalInstance', 'entity', 'ShiftTypeTask'];

    function ShiftTypeTaskDeleteController($uibModalInstance, entity, ShiftTypeTask) {
        var vm = this;
        vm.shiftTypeTask = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            ShiftTypeTask.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
