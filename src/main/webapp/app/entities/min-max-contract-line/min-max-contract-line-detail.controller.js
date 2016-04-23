(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('MinMaxContractLineDetailController', MinMaxContractLineDetailController);

    MinMaxContractLineDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'MinMaxContractLine', 'Contract'];

    function MinMaxContractLineDetailController($scope, $rootScope, $stateParams, entity, MinMaxContractLine, Contract) {
        var vm = this;
        vm.minMaxContractLine = entity;
        
        var unsubscribe = $rootScope.$on('shiftworkApp:minMaxContractLineUpdate', function(event, result) {
            vm.minMaxContractLine = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
