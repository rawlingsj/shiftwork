(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('SkillProficiencyDialogController', SkillProficiencyDialogController);

    SkillProficiencyDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'SkillProficiency', 'Skill', 'Employee'];

    function SkillProficiencyDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, SkillProficiency, Skill, Employee) {
        var vm = this;
        vm.skillProficiency = entity;
        vm.skills = Skill.query({filter: 'skillproficiency-is-null'});
        $q.all([vm.skillProficiency.$promise, vm.skills.$promise]).then(function() {
            if (!vm.skillProficiency.skill || !vm.skillProficiency.skill.id) {
                return $q.reject();
            }
            return Skill.get({id : vm.skillProficiency.skill.id}).$promise;
        }).then(function(skill) {
            vm.skills.push(skill);
        });
        vm.employees = Employee.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('shiftworkApp:skillProficiencyUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.skillProficiency.id !== null) {
                SkillProficiency.update(vm.skillProficiency, onSaveSuccess, onSaveError);
            } else {
                SkillProficiency.save(vm.skillProficiency, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
