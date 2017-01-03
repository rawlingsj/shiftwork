(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('ShiftDateDialogController', ShiftDateDialogController);

    ShiftDateDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ShiftDate', 'Shift'];

    function ShiftDateDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ShiftDate, Shift) {
        var vm = this;
        vm.shiftDate = entity;
        vm.shifts = Shift.query();

        $scope.days = [
            { name: 'S', value: 'SUNDAY',  selected: false },
            { name: 'M', value: 'MONDAY',   selected: false },
            { name: 'T', value: 'TUESDAY',   selected: false },
            { name: 'W', value: 'WEDNESDAY',   selected: false },
            { name: 'T', value: 'THURSDAY',   selected: false },
            { name: 'F', value: 'FRIDAY',   selected: false },
            { name: 'S', value: 'SATURDAY',   selected: false }
        ];

        $scope.selection = [];

        $scope.selectedDays = function selectedDays() {
            return filterFilter($scope.days, { selected: true });
        };

        $scope.$watch('days|filter:{selected:true}', function (nv) {
            $scope.selection = nv.map(function (day) {
                return day.value;
            });
        }, true);

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('shiftworkApp:shiftDateUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            vm.shiftDate.daysOfWeek = $scope.selection;
            if (vm.shiftDate.id !== null) {
                ShiftDate.update(vm.shiftDate, onSaveSuccess, onSaveError);
            } else {
                ShiftDate.save(vm.shiftDate, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.date = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
