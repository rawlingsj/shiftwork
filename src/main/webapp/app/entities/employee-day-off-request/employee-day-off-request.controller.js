(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('EmployeeDayOffRequestController', EmployeeDayOffRequestController);

    EmployeeDayOffRequestController.$inject = ['$scope', '$state', 'EmployeeDayOffRequest'];

    function EmployeeDayOffRequestController ($scope, $state, EmployeeDayOffRequest) {
        var vm = this;
        vm.employeeDayOffRequests = [];
        vm.loadAll = function() {
            EmployeeDayOffRequest.query(function(result) {
                vm.employeeDayOffRequests = result;
            });
        };

        vm.loadAll();
        
    }
})();
