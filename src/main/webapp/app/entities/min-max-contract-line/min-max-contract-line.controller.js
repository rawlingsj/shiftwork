(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('MinMaxContractLineController', MinMaxContractLineController);

    MinMaxContractLineController.$inject = ['$scope', '$state', 'MinMaxContractLine'];

    function MinMaxContractLineController ($scope, $state, MinMaxContractLine) {
        var vm = this;
        vm.minMaxContractLines = [];
        vm.loadAll = function() {
            MinMaxContractLine.query(function(result) {
                vm.minMaxContractLines = result;
            });
        };

        vm.loadAll();
        
    }
})();
