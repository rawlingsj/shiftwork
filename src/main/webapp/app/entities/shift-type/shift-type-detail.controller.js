(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('ShiftTypeDetailController', ShiftTypeDetailController);

    ShiftTypeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'ShiftType', 'Task'];

    function ShiftTypeDetailController($scope, $rootScope, $stateParams, entity, ShiftType, Task) {
        var vm = this;
        vm.shiftType = entity;
        
        var unsubscribe = $rootScope.$on('shiftworkApp:shiftTypeUpdate', function(event, result) {
            vm.shiftType = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
