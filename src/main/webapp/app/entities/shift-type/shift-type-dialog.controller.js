(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('ShiftTypeDialogController', ShiftTypeDialogController);

    ShiftTypeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ShiftType', 'Task'];

    function ShiftTypeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ShiftType, Task) {
        var vm = this;
        vm.shiftType = entity;
        vm.tasks = Task.query();
        vm.duplicateMsg = false;
        vm.editId = $stateParams.id === null ? 0 : parseInt($stateParams.id);
        vm.shiftTypes = [];
        vm.loadAll = function() {
            ShiftType.query(function(result) {
                vm.shiftTypes = result;
            });
        };

        vm.loadAll();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('shiftworkApp:shiftTypeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.shiftType.id !== null) {
                ShiftType.update(vm.shiftType, onSaveSuccess, onSaveError);
            } else {
                ShiftType.save(vm.shiftType, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.verifyDuplicate = function(code) {
            vm.duplicateMsg = false;
            angular.forEach(vm.shiftTypes, function(shiftType, key){
                if(shiftType.code === code) {
                    if( vm.editId === 0) {
                        console.log('existed');
                        vm.duplicateMsg = true;
                        return vm.duplicateMsg;
                    }
                    else if(vm.editId != shiftType.id) {
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
