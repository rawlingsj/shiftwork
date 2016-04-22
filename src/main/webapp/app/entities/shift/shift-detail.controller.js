(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('ShiftDetailController', ShiftDetailController);

    ShiftDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Shift', 'ShiftType', 'ShiftDate'];

    function ShiftDetailController($scope, $rootScope, $stateParams, entity, Shift, ShiftType, ShiftDate) {
        var vm = this;
        vm.shift = entity;
        
        var unsubscribe = $rootScope.$on('shiftworkApp:shiftUpdate', function(event, result) {
            vm.shift = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
