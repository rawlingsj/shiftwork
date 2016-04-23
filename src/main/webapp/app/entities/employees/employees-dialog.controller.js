(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('EmployeesDialogController', EmployeesDialogController);

    EmployeesDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Employees', 'Employee'];

    function EmployeesDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Employees, Employee) {
        var vm = this;
        vm.employees = entity;
        vm.employees = Employee.query({filter: 'employees-is-null'});
        $q.all([vm.employees.$promise, vm.employees.$promise]).then(function() {
            if (!vm.employees.employee || !vm.employees.employee.id) {
                return $q.reject();
            }
            return Employee.get({id : vm.employees.employee.id}).$promise;
        }).then(function(employee) {
            vm.employees.push(employee);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('shiftworkApp:employeesUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.employees.id !== null) {
                Employees.update(vm.employees, onSaveSuccess, onSaveError);
            } else {
                Employees.save(vm.employees, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
