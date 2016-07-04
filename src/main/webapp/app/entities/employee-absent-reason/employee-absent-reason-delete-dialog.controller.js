(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('EmployeeAbsentReasonDeleteController',EmployeeAbsentReasonDeleteController);

    EmployeeAbsentReasonDeleteController.$inject = ['$uibModalInstance', 'entity', 'EmployeeAbsentReason'];

    function EmployeeAbsentReasonDeleteController($uibModalInstance, entity, EmployeeAbsentReason) {
        var vm = this;
        vm.employeeAbsentReason = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            EmployeeAbsentReason.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
