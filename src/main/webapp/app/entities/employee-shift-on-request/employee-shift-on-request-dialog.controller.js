(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('EmployeeShiftOnRequestDialogController', EmployeeShiftOnRequestDialogController);

    EmployeeShiftOnRequestDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'EmployeeShiftOnRequest', 'Shift', 'Employee'];

    function EmployeeShiftOnRequestDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, EmployeeShiftOnRequest, Shift, Employee) {
        var vm = this;
        vm.employeeShiftOnRequest = entity;
        vm.shifts = Shift.query({filter: 'employeeshiftonrequest-is-null'});
        $q.all([vm.employeeShiftOnRequest.$promise, vm.shifts.$promise]).then(function() {
            if (!vm.employeeShiftOnRequest.shift || !vm.employeeShiftOnRequest.shift.id) {
                return $q.reject();
            }
            return Shift.get({id : vm.employeeShiftOnRequest.shift.id}).$promise;
        }).then(function(shift) {
            vm.shifts.push(shift);
        });
        vm.employees = Employee.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('shiftworkApp:employeeShiftOnRequestUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.employeeShiftOnRequest.id !== null) {
                EmployeeShiftOnRequest.update(vm.employeeShiftOnRequest, onSaveSuccess, onSaveError);
            } else {
                EmployeeShiftOnRequest.save(vm.employeeShiftOnRequest, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
