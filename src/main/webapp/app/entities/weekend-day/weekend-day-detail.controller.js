(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('WeekendDayDetailController', WeekendDayDetailController);

    WeekendDayDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'WeekendDay', 'WeekendDefinition'];

    function WeekendDayDetailController($scope, $rootScope, $stateParams, entity, WeekendDay, WeekendDefinition) {
        var vm = this;
        vm.weekendDay = entity;
        
        var unsubscribe = $rootScope.$on('shiftworkApp:weekendDayUpdate', function(event, result) {
            vm.weekendDay = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
