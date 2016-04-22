(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('EmployeeDayOnRequestController', EmployeeDayOnRequestController);

    EmployeeDayOnRequestController.$inject = ['$scope', '$state', 'EmployeeDayOnRequest'];

    function EmployeeDayOnRequestController ($scope, $state, EmployeeDayOnRequest) {
        var vm = this;
        vm.employeeDayOnRequests = [];
        vm.loadAll = function() {
            EmployeeDayOnRequest.query(function(result) {
                vm.employeeDayOnRequests = result;
            });
        };

        vm.loadAll();
        
    }
})();
