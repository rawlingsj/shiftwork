(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('EmployeeShiftOffRequestDetailController', EmployeeShiftOffRequestDetailController);

    EmployeeShiftOffRequestDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'EmployeeShiftOffRequest', 'Shift', 'Employee'];

    function EmployeeShiftOffRequestDetailController($scope, $rootScope, $stateParams, entity, EmployeeShiftOffRequest, Shift, Employee) {
        var vm = this;
        vm.employeeShiftOffRequest = entity;
        
        var unsubscribe = $rootScope.$on('shiftworkApp:employeeShiftOffRequestUpdate', function(event, result) {
            vm.employeeShiftOffRequest = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
