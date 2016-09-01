(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('EmployeeLeaveAbsenceController', EmployeeLeaveAbsenceController);

    EmployeeLeaveAbsenceController.$inject = ['$scope', '$state', 'EmployeeLeaveAbsence'];

    function EmployeeLeaveAbsenceController ($scope, $state, EmployeeLeaveAbsence) {
        var vm = this;
        vm.employeeLeaveAbsences = [];
        vm.loadAll = function() {
            EmployeeLeaveAbsence.query(function(result) {
                vm.employeeLeaveAbsences = result;
            });
        };

        vm.loadAll();
        
    }
})();
