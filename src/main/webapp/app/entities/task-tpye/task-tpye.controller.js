(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('TaskTpyeController', TaskTpyeController);

    TaskTpyeController.$inject = ['$scope', '$state', 'TaskTpye'];

    function TaskTpyeController ($scope, $state, TaskTpye) {
        var vm = this;
        vm.taskTpyes = [];
        vm.loadAll = function() {
            TaskTpye.query(function(result) {
                vm.taskTpyes = result;
            });
        };

        vm.loadAll();
        
    }
})();
