(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('EmployeeAbsentReasonDetailController', EmployeeAbsentReasonDetailController);

    EmployeeAbsentReasonDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'EmployeeAbsentReason'];

    function EmployeeAbsentReasonDetailController($scope, $rootScope, $stateParams, entity, EmployeeAbsentReason) {
        var vm = this;
        vm.employeeAbsentReason = entity;
        
        var unsubscribe = $rootScope.$on('shiftworkApp:employeeAbsentReasonUpdate', function(event, result) {
            vm.employeeAbsentReason = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
