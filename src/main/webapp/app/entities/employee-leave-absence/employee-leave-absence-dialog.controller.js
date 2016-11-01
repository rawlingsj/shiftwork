(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('EmployeeLeaveAbsenceDialogController', EmployeeLeaveAbsenceDialogController);

    EmployeeLeaveAbsenceDialogController.$inject = ['$timeout', '$scope', '$filter', '$stateParams', '$uibModalInstance', 'entity', 'EmployeeLeaveAbsence', 'EmployeeAbsentReason', 'Employee'];

    function EmployeeLeaveAbsenceDialogController ($timeout, $scope, $filter, $stateParams, $uibModalInstance, entity, EmployeeLeaveAbsence, EmployeeAbsentReason, Employee) {
        var vm = this;
        vm.employeeLeaveAbsence = entity;
        vm.employeeabsentreasons = EmployeeAbsentReason.query();
        vm.employees = Employee.query();
        vm.isSaving = false;
        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('shiftworkApp:employeeLeaveAbsenceUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            if (vm.employeeLeaveAbsence.id !== null) {
                EmployeeLeaveAbsence.update(vm.employeeLeaveAbsence, onSaveSuccess, onSaveError);
            } else {
                EmployeeLeaveAbsence.save(vm.employeeLeaveAbsence, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.absentFrom = false;
        vm.datePickerOpenStatus.absentTo = false;
        vm.minDate = new Date();
        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };

        $scope.$watch("vm.employeeLeaveAbsence.absentFrom", function(newValue, oldValue) {
            if(vm.employeeLeaveAbsence.absentFrom == null){
                vm.employeeLeaveAbsence.absentFrom = vm.minDate;
            }
            else if(vm.employeeLeaveAbsence.absentFrom > vm.employeeLeaveAbsence.absentTo) {
                vm.employeeLeaveAbsence.absentTo =  new Date(new Date(vm.employeeLeaveAbsence.absentFrom).getTime() + 24 * 60 * 60 * 1000);
                vm.employeeLeaveAbsence.absentTo.setHours(0,0,0,0);
            }
 
        });

        $scope.$watch("vm.employeeLeaveAbsence.absentTo", function(newValue, oldValue) {
            if(vm.employeeLeaveAbsence.absentTo == null){
                vm.employeeLeaveAbsence.absentTo =  new Date(new Date(vm.employeeLeaveAbsence.absentFrom).getTime() + 24 * 60 * 60 * 1000);
                vm.employeeLeaveAbsence.absentTo.setHours(0,0,0,0);
            }
        });

        if( vm.employeeLeaveAbsence.absentFrom == null) {
            vm.employeeLeaveAbsence.absentFrom = vm.minDate;
        }

        if( vm.employeeLeaveAbsence.absentTo == null) {
            vm.employeeLeaveAbsence.absentTo = new Date(new Date(vm.minDate).getTime() + 24 * 60 * 60 * 1000);
            vm.employeeLeaveAbsence.absentTo.setHours(0,0,0,0);
        }        
        var selectedEmployee = false;
        var selectedReason = false;
        vm.showEmployee = function() {
               var name = '';
               if(vm.employees.length == 0 || vm.employeeLeaveAbsence.employee == null)  {
                name =  'Employee';
                selectedEmployee = false;
               }
               else {
                   var selected = $filter('filter')(vm.employees, {id: vm.employeeLeaveAbsence.employee.id});
                   if(vm.employeeLeaveAbsence.employee.id && selected.length){
                        name = selected[0].name;
                        selectedEmployee = true;
                   }
               }
               if(selectedEmployee && selectedReason) {
                     vm.isSaving = true;
               }
               return name;
        };

        vm.showReason = function() {
                var name = 'Not set';
                selectedReason = true;
                if(vm.employeeabsentreasons.length == 0)  {
                    selectedReason = false;
                }
                else if(vm.employeeLeaveAbsence.reason == null){
                    name = vm.employeeabsentreasons[0].name;//select default value
                    vm.employeeLeaveAbsence.reason = vm.employeeabsentreasons[0];
                }
                else{
                    var selected = $filter('filter')(vm.employeeabsentreasons, {id: vm.employeeLeaveAbsence.reason.id});
                    if(vm.employeeLeaveAbsence.reason.id && selected.length){
                       name = selected[0].name;
                    }
               }
               if(selectedEmployee && selectedReason) {
                    vm.isSaving = true;
               } 
               return name;
        };

    }
})();
