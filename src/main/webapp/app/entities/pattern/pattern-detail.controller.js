(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('PatternDetailController', PatternDetailController);

    PatternDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Pattern'];

    function PatternDetailController($scope, $rootScope, $stateParams, entity, Pattern) {
        var vm = this;
        vm.pattern = entity;
        
        var unsubscribe = $rootScope.$on('shiftworkApp:patternUpdate', function(event, result) {
            vm.pattern = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
