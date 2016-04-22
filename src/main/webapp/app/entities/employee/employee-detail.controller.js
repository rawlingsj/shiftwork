(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('EmployeeDetailController', EmployeeDetailController);

    EmployeeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Employee', 'Contract', 'WeekendDefinition', 'SkillProficiency', 'EmployeeDayOffRequest', 'EmployeeDayOnRequest', 'EmployeeShiftOffRequest', 'EmployeeShiftOnRequest'];

    function EmployeeDetailController($scope, $rootScope, $stateParams, entity, Employee, Contract, WeekendDefinition, SkillProficiency, EmployeeDayOffRequest, EmployeeDayOnRequest, EmployeeShiftOffRequest, EmployeeShiftOnRequest) {
        var vm = this;
        vm.employee = entity;
        
        var unsubscribe = $rootScope.$on('shiftworkApp:employeeUpdate', function(event, result) {
            vm.employee = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
