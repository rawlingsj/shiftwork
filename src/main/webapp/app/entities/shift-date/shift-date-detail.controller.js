(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('ShiftDateDetailController', ShiftDateDetailController);

    ShiftDateDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'ShiftDate', 'Shift'];

    function ShiftDateDetailController($scope, $rootScope, $stateParams, entity, ShiftDate, Shift) {
        var vm = this;
        vm.shiftDate = entity;
        
        var unsubscribe = $rootScope.$on('shiftworkApp:shiftDateUpdate', function(event, result) {
            vm.shiftDate = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
