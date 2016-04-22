(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('EmployeeController', EmployeeController);

    EmployeeController.$inject = ['$scope', '$state', 'Employee'];

    function EmployeeController ($scope, $state, Employee) {
        var vm = this;
        vm.employees = [];
        vm.loadAll = function() {
            Employee.query(function(result) {
                vm.employees = result;
            });
        };

        vm.loadAll();
        
    }
})();
