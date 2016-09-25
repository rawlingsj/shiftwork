(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('EmployeeLeaveAbsenceDetailController', EmployeeLeaveAbsenceDetailController);

    EmployeeLeaveAbsenceDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'EmployeeLeaveAbsence', 'EmployeeAbsentReason', 'Employee'];

    function EmployeeLeaveAbsenceDetailController($scope, $rootScope, $stateParams, entity, EmployeeLeaveAbsence, EmployeeAbsentReason, Employee) {
        var vm = this;
        vm.employeeLeaveAbsence = entity;
        
        var unsubscribe = $rootScope.$on('shiftworkApp:employeeLeaveAbsenceUpdate', function(event, result) {
            vm.employeeLeaveAbsence = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
