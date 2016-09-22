(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('EmployeeDialogController', EmployeeDialogController);

    EmployeeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Employee', 'Contract', 'EmployeeDayOffRequest', 'EmployeeDayOnRequest', 'EmployeeShiftOffRequest', 'EmployeeShiftOnRequest', 'EmployeeLeaveAbsence'];

    function EmployeeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Employee, Contract, EmployeeDayOffRequest, EmployeeDayOnRequest, EmployeeShiftOffRequest, EmployeeShiftOnRequest, EmployeeLeaveAbsence) {
        var vm = this;
        vm.employee = entity;
        vm.contracts = Contract.query();
        vm.employeedayoffrequests = EmployeeDayOffRequest.query();
        vm.employeedayonrequests = EmployeeDayOnRequest.query();
        vm.employeeshiftoffrequests = EmployeeShiftOffRequest.query();
        vm.employeeshiftonrequests = EmployeeShiftOnRequest.query();
        vm.employeeleaveabsences = EmployeeLeaveAbsence.query();
        vm.duplicateMsg = false;
        vm.editId = $stateParams.id === null ? 0 : parseInt($stateParams.id);
        vm.employees = [];
        vm.loadAll = function() {
            Employee.query(function(result) {
                vm.employees = result;
            });
        };

        vm.loadAll();

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

        vm.verifyDuplicate = function(code) {
            vm.duplicateMsg = false;
            angular.forEach(vm.employees, function(employee, key){
                if(employee.code === code) {
                    if( vm.editId === 0) {
                        console.log('existed');
                        vm.duplicateMsg = true;
                        return vm.duplicateMsg;
                    }
                    else if(vm.editId != employee.id) {
                        console.log('existed');
                        vm.duplicateMsg = true;
                        return vm.duplicateMsg;                       
                    }
                }
            });
            return vm.duplicateMsg;
        }       
    }
})();
