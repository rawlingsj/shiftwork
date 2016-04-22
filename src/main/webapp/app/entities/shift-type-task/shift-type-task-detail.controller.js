(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('ShiftTypeTaskDetailController', ShiftTypeTaskDetailController);

    ShiftTypeTaskDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'ShiftTypeTask', 'Task', 'ShiftType'];

    function ShiftTypeTaskDetailController($scope, $rootScope, $stateParams, entity, ShiftTypeTask, Task, ShiftType) {
        var vm = this;
        vm.shiftTypeTask = entity;
        
        var unsubscribe = $rootScope.$on('shiftworkApp:shiftTypeTaskUpdate', function(event, result) {
            vm.shiftTypeTask = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
