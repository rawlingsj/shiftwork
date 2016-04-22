(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('WeekendDefinitionDeleteController',WeekendDefinitionDeleteController);

    WeekendDefinitionDeleteController.$inject = ['$uibModalInstance', 'entity', 'WeekendDefinition'];

    function WeekendDefinitionDeleteController($uibModalInstance, entity, WeekendDefinition) {
        var vm = this;
        vm.weekendDefinition = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            WeekendDefinition.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
