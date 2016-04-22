(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('TaskTpyeDetailController', TaskTpyeDetailController);

    TaskTpyeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'TaskTpye'];

    function TaskTpyeDetailController($scope, $rootScope, $stateParams, entity, TaskTpye) {
        var vm = this;
        vm.taskTpye = entity;
        
        var unsubscribe = $rootScope.$on('shiftworkApp:taskTpyeUpdate', function(event, result) {
            vm.taskTpye = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
