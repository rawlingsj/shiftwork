(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('PlanningJobDetailController', PlanningJobDetailController);

    PlanningJobDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'PlanningJob', 'StaffRosterParametrization'];

    function PlanningJobDetailController($scope, $rootScope, $stateParams, entity, PlanningJob, StaffRosterParametrization) {
        var vm = this;
        vm.planningJob = entity;
        
        var unsubscribe = $rootScope.$on('shiftworkApp:planningJobUpdate', function(event, result) {
            vm.planningJob = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
