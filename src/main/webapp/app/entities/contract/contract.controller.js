(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('ContractController', ContractController);

    ContractController.$inject = ['$scope', '$state', 'Contract'];

    function ContractController ($scope, $state, Contract) {
        var vm = this;
        vm.contracts = [];
        vm.loadAll = function() {
            Contract.query(function(result) {
                vm.contracts = result;
            });
        };

        vm.loadAll();
        
    }
})();
