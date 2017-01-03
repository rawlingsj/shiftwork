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
            { name: 'Mon', value: 'MONDAY',   selected: false },
            { name: 'Tue', value: 'TUESDAY',   selected: false },
            { name: 'Wed', value: 'WEDNESDAY',   selected: false },
            { name: 'Thu', value: 'THURSDAY',   selected: false },
            { name: 'Fri', value: 'FRIDAY',   selected: false },
            { name: 'Sat', value: 'SATURDAY',   selected: false },
            { name: 'Sun', value: 'SUNDAY',  selected: false }
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
