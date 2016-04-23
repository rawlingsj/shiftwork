(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('BooleanContractLineDetailController', BooleanContractLineDetailController);

    BooleanContractLineDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'BooleanContractLine', 'Contract'];

    function BooleanContractLineDetailController($scope, $rootScope, $stateParams, entity, BooleanContractLine, Contract) {
        var vm = this;
        vm.booleanContractLine = entity;
        
        var unsubscribe = $rootScope.$on('shiftworkApp:booleanContractLineUpdate', function(event, result) {
            vm.booleanContractLine = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
