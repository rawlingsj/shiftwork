(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('PatternContractLineController', PatternContractLineController);

    PatternContractLineController.$inject = ['$scope', '$state', 'PatternContractLine'];

    function PatternContractLineController ($scope, $state, PatternContractLine) {
        var vm = this;
        vm.patternContractLines = [];
        vm.loadAll = function() {
            PatternContractLine.query(function(result) {
                vm.patternContractLines = result;
            });
        };

        vm.loadAll();
        
    }
})();
