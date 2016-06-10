(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('SkillProficiencyDialogController', SkillProficiencyDialogController);

    SkillProficiencyDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'SkillProficiency', 'Employee', 'Skill'];

    function SkillProficiencyDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, SkillProficiency, Employee, Skill) {
        var vm = this;

        vm.skillProficiency = entity;
        vm.clear = clear;
        vm.save = save;
        vm.employees = Employee.query({filter: 'skillproficiency-is-null'});
        $q.all([vm.skillProficiency.$promise, vm.employees.$promise]).then(function() {
            if (!vm.skillProficiency.employee || !vm.skillProficiency.employee.id) {
                return $q.reject();
            }
            return Employee.get({id : vm.skillProficiency.employee.id}).$promise;
        }).then(function(employee) {
            vm.employees.push(employee);
        });
        vm.skills = Skill.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.skillProficiency.id !== null) {
                SkillProficiency.update(vm.skillProficiency, onSaveSuccess, onSaveError);
            } else {
                SkillProficiency.save(vm.skillProficiency, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('shiftworkApp:skillProficiencyUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
