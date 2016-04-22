(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('PatternController', PatternController);

    PatternController.$inject = ['$scope', '$state', 'Pattern'];

    function PatternController ($scope, $state, Pattern) {
        var vm = this;
        vm.patterns = [];
        vm.loadAll = function() {
            Pattern.query(function(result) {
                vm.patterns = result;
            });
        };

        vm.loadAll();
        
    }
})();
