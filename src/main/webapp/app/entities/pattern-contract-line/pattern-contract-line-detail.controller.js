(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('PatternContractLineDetailController', PatternContractLineDetailController);

    PatternContractLineDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'PatternContractLine', 'Contract', 'Pattern'];

    function PatternContractLineDetailController($scope, $rootScope, $stateParams, entity, PatternContractLine, Contract, Pattern) {
        var vm = this;
        vm.patternContractLine = entity;
        
        var unsubscribe = $rootScope.$on('shiftworkApp:patternContractLineUpdate', function(event, result) {
            vm.patternContractLine = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
