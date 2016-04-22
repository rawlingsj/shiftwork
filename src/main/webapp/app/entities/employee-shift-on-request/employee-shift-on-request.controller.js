(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('EmployeeShiftOnRequestController', EmployeeShiftOnRequestController);

    EmployeeShiftOnRequestController.$inject = ['$scope', '$state', 'EmployeeShiftOnRequest'];

    function EmployeeShiftOnRequestController ($scope, $state, EmployeeShiftOnRequest) {
        var vm = this;
        vm.employeeShiftOnRequests = [];
        vm.loadAll = function() {
            EmployeeShiftOnRequest.query(function(result) {
                vm.employeeShiftOnRequests = result;
            });
        };

        vm.loadAll();
        
    }
})();
