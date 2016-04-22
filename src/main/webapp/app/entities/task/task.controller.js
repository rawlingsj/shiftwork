(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('TaskController', TaskController);

    TaskController.$inject = ['$scope', '$state', 'Task'];

    function TaskController ($scope, $state, Task) {
        var vm = this;
        vm.tasks = [];
        vm.loadAll = function() {
            Task.query(function(result) {
                vm.tasks = result;
            });
        };

        vm.loadAll();
        
    }
})();
