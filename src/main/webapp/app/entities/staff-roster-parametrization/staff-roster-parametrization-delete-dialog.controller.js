(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('StaffRosterParametrizationDeleteController',StaffRosterParametrizationDeleteController);

    StaffRosterParametrizationDeleteController.$inject = ['$uibModalInstance', 'entity', 'StaffRosterParametrization'];

    function StaffRosterParametrizationDeleteController($uibModalInstance, entity, StaffRosterParametrization) {
        var vm = this;
        vm.staffRosterParametrization = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            StaffRosterParametrization.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
