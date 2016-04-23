(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('EmployeesDeleteController',EmployeesDeleteController);

    EmployeesDeleteController.$inject = ['$uibModalInstance', 'entity', 'Employees'];

    function EmployeesDeleteController($uibModalInstance, entity, Employees) {
        var vm = this;
        vm.employees = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Employees.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
