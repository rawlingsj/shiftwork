(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('PatternDeleteController',PatternDeleteController);

    PatternDeleteController.$inject = ['$uibModalInstance', 'entity', 'Pattern'];

    function PatternDeleteController($uibModalInstance, entity, Pattern) {
        var vm = this;
        vm.pattern = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Pattern.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
