(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('EmployeeShiftOffRequestDeleteController',EmployeeShiftOffRequestDeleteController);

    EmployeeShiftOffRequestDeleteController.$inject = ['$uibModalInstance', 'entity', 'EmployeeShiftOffRequest'];

    function EmployeeShiftOffRequestDeleteController($uibModalInstance, entity, EmployeeShiftOffRequest) {
        var vm = this;
        vm.employeeShiftOffRequest = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            EmployeeShiftOffRequest.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
