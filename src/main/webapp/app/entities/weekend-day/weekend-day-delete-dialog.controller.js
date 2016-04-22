(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('WeekendDayDeleteController',WeekendDayDeleteController);

    WeekendDayDeleteController.$inject = ['$uibModalInstance', 'entity', 'WeekendDay'];

    function WeekendDayDeleteController($uibModalInstance, entity, WeekendDay) {
        var vm = this;
        vm.weekendDay = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            WeekendDay.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
