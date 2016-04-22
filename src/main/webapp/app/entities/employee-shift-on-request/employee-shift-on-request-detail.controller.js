(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('EmployeeShiftOnRequestDetailController', EmployeeShiftOnRequestDetailController);

    EmployeeShiftOnRequestDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'EmployeeShiftOnRequest', 'Shift', 'Employee'];

    function EmployeeShiftOnRequestDetailController($scope, $rootScope, $stateParams, entity, EmployeeShiftOnRequest, Shift, Employee) {
        var vm = this;
        vm.employeeShiftOnRequest = entity;
        
        var unsubscribe = $rootScope.$on('shiftworkApp:employeeShiftOnRequestUpdate', function(event, result) {
            vm.employeeShiftOnRequest = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
