(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('TaskTpyeDeleteController',TaskTpyeDeleteController);

    TaskTpyeDeleteController.$inject = ['$uibModalInstance', 'entity', 'TaskTpye'];

    function TaskTpyeDeleteController($uibModalInstance, entity, TaskTpye) {
        var vm = this;
        vm.taskTpye = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            TaskTpye.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
