(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('EmployeesController', EmployeesController);

    EmployeesController.$inject = ['$scope', '$state', 'Employees'];

    function EmployeesController ($scope, $state, Employees) {
        var vm = this;
        vm.employees = [];
        vm.loadAll = function() {
            Employees.query(function(result) {
                vm.employees = result;
            });
        };

        vm.loadAll();
        
    }
})();
