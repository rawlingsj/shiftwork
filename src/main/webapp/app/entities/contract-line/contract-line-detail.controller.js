(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('ContractLineDetailController', ContractLineDetailController);

    ContractLineDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'ContractLine', 'Contract'];

    function ContractLineDetailController($scope, $rootScope, $stateParams, entity, ContractLine, Contract) {
        var vm = this;
        vm.contractLine = entity;
        
        var unsubscribe = $rootScope.$on('shiftworkApp:contractLineUpdate', function(event, result) {
            vm.contractLine = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
