(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('EmployeeAbsentReasonController', EmployeeAbsentReasonController);

    EmployeeAbsentReasonController.$inject = ['$scope', '$state', 'EmployeeAbsentReason'];

    function EmployeeAbsentReasonController ($scope, $state, EmployeeAbsentReason) {
        var vm = this;
        vm.employeeAbsentReasons = [];
        vm.loadAll = function() {
            EmployeeAbsentReason.query(function(result) {
                vm.employeeAbsentReasons = result;
            });
        };

        vm.loadAll();
        
    }
})();
