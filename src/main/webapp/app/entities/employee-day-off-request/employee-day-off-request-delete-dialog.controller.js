(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('EmployeeDayOffRequestDeleteController',EmployeeDayOffRequestDeleteController);

    EmployeeDayOffRequestDeleteController.$inject = ['$uibModalInstance', 'entity', 'EmployeeDayOffRequest'];

    function EmployeeDayOffRequestDeleteController($uibModalInstance, entity, EmployeeDayOffRequest) {
        var vm = this;
        vm.employeeDayOffRequest = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            EmployeeDayOffRequest.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
