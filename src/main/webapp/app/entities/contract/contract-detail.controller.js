(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('ContractDetailController', ContractDetailController);

    ContractDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Contract', 'ContractLine', 'WeekendDefinition'];

    function ContractDetailController($scope, $rootScope, $stateParams, entity, Contract, ContractLine, WeekendDefinition) {
        var vm = this;
        vm.contract = entity;
        
        var unsubscribe = $rootScope.$on('shiftworkApp:contractUpdate', function(event, result) {
            vm.contract = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
