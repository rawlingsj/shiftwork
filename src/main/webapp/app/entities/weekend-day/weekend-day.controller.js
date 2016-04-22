(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('WeekendDayController', WeekendDayController);

    WeekendDayController.$inject = ['$scope', '$state', 'WeekendDay'];

    function WeekendDayController ($scope, $state, WeekendDay) {
        var vm = this;
        vm.weekendDays = [];
        vm.loadAll = function() {
            WeekendDay.query(function(result) {
                vm.weekendDays = result;
            });
        };

        vm.loadAll();
        
    }
})();
