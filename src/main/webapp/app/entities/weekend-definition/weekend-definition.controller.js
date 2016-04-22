(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('WeekendDefinitionController', WeekendDefinitionController);

    WeekendDefinitionController.$inject = ['$scope', '$state', 'WeekendDefinition'];

    function WeekendDefinitionController ($scope, $state, WeekendDefinition) {
        var vm = this;
        vm.weekendDefinitions = [];
        vm.loadAll = function() {
            WeekendDefinition.query(function(result) {
                vm.weekendDefinitions = result;
            });
        };

        vm.loadAll();
        
    }
})();
