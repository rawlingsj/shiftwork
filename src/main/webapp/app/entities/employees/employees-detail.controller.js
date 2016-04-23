(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('EmployeesDetailController', EmployeesDetailController);

    EmployeesDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Employees', 'Employee'];

    function EmployeesDetailController($scope, $rootScope, $stateParams, entity, Employees, Employee) {
        var vm = this;
        vm.employees = entity;
        
        var unsubscribe = $rootScope.$on('shiftworkApp:employeesUpdate', function(event, result) {
            vm.employees = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
