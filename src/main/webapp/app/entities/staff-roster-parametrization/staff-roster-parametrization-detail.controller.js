(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('StaffRosterParametrizationDetailController', StaffRosterParametrizationDetailController);

    StaffRosterParametrizationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'StaffRosterParametrization', 'ShiftDate'];

    function StaffRosterParametrizationDetailController($scope, $rootScope, $stateParams, entity, StaffRosterParametrization, ShiftDate) {
        var vm = this;
        vm.staffRosterParametrization = entity;
        
        var unsubscribe = $rootScope.$on('shiftworkApp:staffRosterParametrizationUpdate', function(event, result) {
            vm.staffRosterParametrization = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
