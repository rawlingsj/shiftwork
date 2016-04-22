(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('ContractLineController', ContractLineController);

    ContractLineController.$inject = ['$scope', '$state', 'ContractLine'];

    function ContractLineController ($scope, $state, ContractLine) {
        var vm = this;
        vm.contractLines = [];
        vm.loadAll = function() {
            ContractLine.query(function(result) {
                vm.contractLines = result;
            });
        };

        vm.loadAll();
        
    }
})();
