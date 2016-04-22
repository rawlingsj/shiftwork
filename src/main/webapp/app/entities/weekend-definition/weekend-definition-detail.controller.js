(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('WeekendDefinitionDetailController', WeekendDefinitionDetailController);

    WeekendDefinitionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'WeekendDefinition', 'WeekendDay'];

    function WeekendDefinitionDetailController($scope, $rootScope, $stateParams, entity, WeekendDefinition, WeekendDay) {
        var vm = this;
        vm.weekendDefinition = entity;
        
        var unsubscribe = $rootScope.$on('shiftworkApp:weekendDefinitionUpdate', function(event, result) {
            vm.weekendDefinition = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
