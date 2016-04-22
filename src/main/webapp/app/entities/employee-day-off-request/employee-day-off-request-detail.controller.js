(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('EmployeeDayOffRequestDetailController', EmployeeDayOffRequestDetailController);

    EmployeeDayOffRequestDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'EmployeeDayOffRequest', 'ShiftDate', 'Employee'];

    function EmployeeDayOffRequestDetailController($scope, $rootScope, $stateParams, entity, EmployeeDayOffRequest, ShiftDate, Employee) {
        var vm = this;
        vm.employeeDayOffRequest = entity;
        
        var unsubscribe = $rootScope.$on('shiftworkApp:employeeDayOffRequestUpdate', function(event, result) {
            vm.employeeDayOffRequest = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
