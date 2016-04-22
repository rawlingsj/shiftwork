(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('EmployeeShiftOffRequestController', EmployeeShiftOffRequestController);

    EmployeeShiftOffRequestController.$inject = ['$scope', '$state', 'EmployeeShiftOffRequest'];

    function EmployeeShiftOffRequestController ($scope, $state, EmployeeShiftOffRequest) {
        var vm = this;
        vm.employeeShiftOffRequests = [];
        vm.loadAll = function() {
            EmployeeShiftOffRequest.query(function(result) {
                vm.employeeShiftOffRequests = result;
            });
        };

        vm.loadAll();
        
    }
})();
