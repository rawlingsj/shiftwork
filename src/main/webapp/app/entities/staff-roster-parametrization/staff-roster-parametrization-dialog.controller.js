(function () {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('StaffRosterParametrizationDialogController', StaffRosterParametrizationDialogController);

    StaffRosterParametrizationDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'StaffRosterParametrization', 'ShiftDate'];

    function StaffRosterParametrizationDialogController($timeout, $scope, $stateParams, $uibModalInstance, entity, StaffRosterParametrization, ShiftDate) {
        var vm = this;
        vm.staffRosterParametrization = entity;
        vm.shiftdates = ShiftDate.query();
        vm.switchTemplate = switchTemplate;
        vm.showHideForm = false;

        $timeout(function () {
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('shiftworkApp:staffRosterParametrizationUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.staffRosterParametrization.id !== null) {
                StaffRosterParametrization.update(vm.staffRosterParametrization, onSaveSuccess, onSaveError);
            } else {
                StaffRosterParametrization.save(vm.staffRosterParametrization, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function () {
            $uibModalInstance.dismiss('cancel');
        };

        function switchTemplate(template) {
            var today = new Date();
            var tomorrow = new Date();
            tomorrow.setDate(today.getDate() + 1);
            var oneWeek = new Date()
            oneWeek.setDate(today.getDate() + 7);
            var oneMonth = new Date()
            oneMonth.setMonth(today.getMonth() + 1);

            var maxItem;
            switch (template) {
                case "t_today": {
                    maxItem = 0;
                    break;
                }
                case "t_tomorrow": {
                    maxItem = 1;
                    break;
                }
                case "t_one_week": {
                    maxItem = 7;
                    break;
                }
                case "t_one_month": {
                    maxItem = 30; //TODO find max for given month
                    break;
                }
            }

            var todaysItem;
            var todaysIndex;
            if (vm.shiftdates.length < maxItem) {
                maxItem = vm.shiftdates.length;
            }

            for (var i = 0; i <= maxItem; i++) {
                todaysItem = vm.shiftdates[i];
                console.log(new Date(today.yyyymmdd()).valueOf() + ' ' + new Date(todaysItem.date).valueOf());
                if (new Date(today.yyyymmdd()).valueOf() == new Date(todaysItem.date).valueOf()) {
                    todaysIndex = i;
                    break;
                }
            }

            vm.staffRosterParametrization.name = template + today.getTime();
            vm.staffRosterParametrization.description = vm.staffRosterParametrization.name;

            vm.staffRosterParametrization.firstShiftDate = todaysItem;
            vm.staffRosterParametrization.lastShiftDate = todaysItem;
            vm.staffRosterParametrization.planningWindowStart = todaysItem;
            vm.staffRosterParametrization.planningWindowEnd = todaysItem;

        }

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.lastRunTime = false;

        vm.openCalendar = function (date) {
            vm.datePickerOpenStatus[date] = true;
        };

        Date.prototype.yyyymmdd = function () {
            var mm = this.getMonth() + 1;
            var dd = this.getDate();

            return [this.getFullYear(),
                (mm > 9 ? '' : '0') + mm,
                (dd > 9 ? '' : '0') + dd
            ].join('-');
        };
    }
})();
