(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('EmployeeDayOnRequestDetailController', EmployeeDayOnRequestDetailController);

    EmployeeDayOnRequestDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'EmployeeDayOnRequest', 'ShiftDate', 'Employee'];

    function EmployeeDayOnRequestDetailController($scope, $rootScope, $stateParams, entity, EmployeeDayOnRequest, ShiftDate, Employee) {
        var vm = this;
        vm.employeeDayOnRequest = entity;
        
        var unsubscribe = $rootScope.$on('shiftworkApp:employeeDayOnRequestUpdate', function(event, result) {
            vm.employeeDayOnRequest = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
