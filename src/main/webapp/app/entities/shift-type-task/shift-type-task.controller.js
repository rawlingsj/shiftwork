(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('ShiftTypeTaskController', ShiftTypeTaskController);

    ShiftTypeTaskController.$inject = ['$scope', '$state', 'ShiftTypeTask'];

    function ShiftTypeTaskController ($scope, $state, ShiftTypeTask) {
        var vm = this;
        vm.shiftTypeTasks = [];
        vm.loadAll = function() {
            ShiftTypeTask.query(function(result) {
                vm.shiftTypeTasks = result;
            });
        };

        vm.loadAll();
        
    }
})();
