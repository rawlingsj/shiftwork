(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('EmployeeLeaveAbsenceDeleteController',EmployeeLeaveAbsenceDeleteController);

    EmployeeLeaveAbsenceDeleteController.$inject = ['$uibModalInstance', 'entity', 'EmployeeLeaveAbsence'];

    function EmployeeLeaveAbsenceDeleteController($uibModalInstance, entity, EmployeeLeaveAbsence) {
        var vm = this;
        vm.employeeLeaveAbsence = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            EmployeeLeaveAbsence.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
