(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('BooleanContractLineController', BooleanContractLineController);

    BooleanContractLineController.$inject = ['$scope', '$state', 'BooleanContractLine'];

    function BooleanContractLineController ($scope, $state, BooleanContractLine) {
        var vm = this;
        vm.booleanContractLines = [];
        vm.loadAll = function() {
            BooleanContractLine.query(function(result) {
                vm.booleanContractLines = result;
            });
        };

        vm.loadAll();
        
    }
})();
