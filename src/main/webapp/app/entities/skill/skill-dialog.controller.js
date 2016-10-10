(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('SkillDialogController', SkillDialogController);

    SkillDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Skill'];

    function SkillDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Skill) {
        var vm = this;
        vm.skill = entity;
        vm.duplicateMsg = false;
        vm.editId = $stateParams.id === null ? 0 : parseInt($stateParams.id);
        vm.skills = [];
        vm.loadAll = function() {
            Skill.query(function(result) {
                vm.skills = result;
            });
        };

        vm.loadAll();
        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('shiftworkApp:skillUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.skill.id !== null) {
                Skill.update(vm.skill, onSaveSuccess, onSaveError);
            } else {
                Skill.save(vm.skill, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.verifyDuplicate = function(code) {
            vm.duplicateMsg = false;
            angular.forEach(vm.skills, function(skill, key){
                if(skill.code === code) {
                    if( vm.editId === 0) {
                        console.log('existed');
                        vm.duplicateMsg = true;
                        return vm.duplicateMsg;
                    }
                    else if(vm.editId != skill.id) {
                        console.log('existed');
                        vm.duplicateMsg = true;
                        return vm.duplicateMsg;                       
                    }
                }
            });
            return vm.duplicateMsg;
        }        
    }
})();
