(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('EmployeeShiftOnRequestDeleteController',EmployeeShiftOnRequestDeleteController);

    EmployeeShiftOnRequestDeleteController.$inject = ['$uibModalInstance', 'entity', 'EmployeeShiftOnRequest'];

    function EmployeeShiftOnRequestDeleteController($uibModalInstance, entity, EmployeeShiftOnRequest) {
        var vm = this;
        vm.employeeShiftOnRequest = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            EmployeeShiftOnRequest.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
