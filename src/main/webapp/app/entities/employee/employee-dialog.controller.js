(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('EmployeeDialogController', EmployeeDialogController);

    EmployeeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Employee', 'Contract', 'WeekendDefinition', 'SkillProficiency', 'EmployeeDayOffRequest', 'EmployeeDayOnRequest', 'EmployeeShiftOffRequest', 'EmployeeShiftOnRequest'];

    function EmployeeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Employee, Contract, WeekendDefinition, SkillProficiency, EmployeeDayOffRequest, EmployeeDayOnRequest, EmployeeShiftOffRequest, EmployeeShiftOnRequest) {
        var vm = this;
        vm.employee = entity;
        vm.contracts = Contract.query({filter: 'employee-is-null'});
        $q.all([vm.employee.$promise, vm.contracts.$promise]).then(function() {
            if (!vm.employee.contract || !vm.employee.contract.id) {
                return $q.reject();
            }
            return Contract.get({id : vm.employee.contract.id}).$promise;
        }).then(function(contract) {
            vm.contracts.push(contract);
        });
        vm.weekenddefinitions = WeekendDefinition.query({filter: 'employee-is-null'});
        $q.all([vm.employee.$promise, vm.weekenddefinitions.$promise]).then(function() {
            if (!vm.employee.weekendDefinition || !vm.employee.weekendDefinition.id) {
                return $q.reject();
            }
            return WeekendDefinition.get({id : vm.employee.weekendDefinition.id}).$promise;
        }).then(function(weekendDefinition) {
            vm.weekenddefinitions.push(weekendDefinition);
        });
        vm.skillproficiencies = SkillProficiency.query();
        vm.employeedayoffrequests = EmployeeDayOffRequest.query();
        vm.employeedayonrequests = EmployeeDayOnRequest.query();
        vm.employeeshiftoffrequests = EmployeeShiftOffRequest.query();
        vm.employeeshiftonrequests = EmployeeShiftOnRequest.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('shiftworkApp:employeeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.employee.id !== null) {
                Employee.update(vm.employee, onSaveSuccess, onSaveError);
            } else {
                Employee.save(vm.employee, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
