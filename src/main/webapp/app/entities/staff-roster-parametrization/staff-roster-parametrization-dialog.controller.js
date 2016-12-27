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
        vm.showHideForm = true;

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
            switch (template) {
                case "TODAY": {
                    setFormValues(today, today, template);
                    break;
                }
                case "TOMORROW": {
                    setFormValues(today, tomorrow, template);
                    break;
                }
                case "ONE WEEK": {
                    setFormValues(today, oneWeek, template);
                    break;
                }
                case "ONE MONTH": {
                    setFormValues(today, oneMonth, template);
                    break;
                }
            }
        }

        function setFormValues(today, lsd, template) {
            vm.staffRosterParametrization.name = template + ' ' + vm.shiftdates[geteDateShiftItem(today)].date;
            vm.staffRosterParametrization.description = template + ' ' + vm.shiftdates[geteDateShiftItem(today)].date;

            vm.staffRosterParametrization.firstShiftDate = vm.shiftdates[geteDateShiftItem(today)];
            vm.staffRosterParametrization.lastShiftDate = vm.shiftdates[geteDateShiftItem(lsd)];
            vm.staffRosterParametrization.planningWindowStart = vm.shiftdates[geteDateShiftItem(today)];
            vm.staffRosterParametrization.planningWindowEnd = vm.shiftdates[geteDateShiftItem(lsd)];
        }

        function geteDateShiftItem(givenDate) {
            for (var i = 0; i < vm.shiftdates.length; i++) {
                if (new Date(givenDate.yyyymmdd()).valueOf() == new Date(vm.shiftdates[i].date).valueOf()) {
                    return i;
                    break;
                }
            }
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
