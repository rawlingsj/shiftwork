(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('EmployeeDialogController', EmployeeDialogController);

    EmployeeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Employee', 'Contract', 'EmployeeDayOffRequest', 'EmployeeDayOnRequest', 'EmployeeShiftOffRequest', 'EmployeeShiftOnRequest', 'ShiftDate', 'ShiftType', 'EmployeeLeaveAbsence'];

    function EmployeeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Employee, Contract, EmployeeDayOffRequest, EmployeeDayOnRequest, EmployeeShiftOffRequest, EmployeeShiftOnRequest, ShiftDate, ShiftType, EmployeeLeaveAbsence) {
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
        vm.employeedayoffrequests = EmployeeDayOffRequest.query();
        vm.employeedayonrequests = EmployeeDayOnRequest.query();
        vm.employeeshiftoffrequests = EmployeeShiftOffRequest.query();
        vm.employeeshiftonrequests = EmployeeShiftOnRequest.query();
        vm.unavailableshiftdates = ShiftDate.query({filter: 'employee-is-null'});
        $q.all([vm.employee.$promise, vm.unavailableshiftdates.$promise]).then(function() {
            if (!vm.employee.unavailableShiftDate || !vm.employee.unavailableShiftDate.id) {
                return $q.reject();
            }
            return ShiftDate.get({id : vm.employee.unavailableShiftDate.id}).$promise;
        }).then(function(unavailableShiftDate) {
            vm.unavailableshiftdates.push(unavailableShiftDate);
        });
        vm.unavailableshifttypes = ShiftType.query({filter: 'employee-is-null'});
        $q.all([vm.employee.$promise, vm.unavailableshifttypes.$promise]).then(function() {
            if (!vm.employee.unavailableShiftType || !vm.employee.unavailableShiftType.id) {
                return $q.reject();
            }
            return ShiftType.get({id : vm.employee.unavailableShiftType.id}).$promise;
        }).then(function(unavailableShiftType) {
            vm.unavailableshifttypes.push(unavailableShiftType);
        });
        vm.employeeleaveabsences = EmployeeLeaveAbsence.query();

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
