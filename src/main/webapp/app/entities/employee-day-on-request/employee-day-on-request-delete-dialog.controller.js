(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('EmployeeDayOnRequestDeleteController',EmployeeDayOnRequestDeleteController);

    EmployeeDayOnRequestDeleteController.$inject = ['$uibModalInstance', 'entity', 'EmployeeDayOnRequest'];

    function EmployeeDayOnRequestDeleteController($uibModalInstance, entity, EmployeeDayOnRequest) {
        var vm = this;
        vm.employeeDayOnRequest = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            EmployeeDayOnRequest.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
