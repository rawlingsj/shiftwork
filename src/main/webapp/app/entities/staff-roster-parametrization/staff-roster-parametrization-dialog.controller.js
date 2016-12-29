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
        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.lastRunTime = false;

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
            switch (template) {
                case "TODAY": {
                    setFormValues(today, today, template);
                    break;
                }
                case "TOMORROW": {
                    var tomorrow = new Date();
                    tomorrow.setDate(today.getDate() + 1);
                    setFormValues(today, tomorrow, template);
                    break;
                }
                case "ONE WEEK": {
                    var oneWeek = new Date()
                    oneWeek.setDate(today.getDate() + 7);
                    setFormValues(today, oneWeek, template);
                    break;
                }
                case "ONE MONTH": {
                    var oneMonth = new Date()
                    oneMonth.setMonth(today.getMonth() + 1);
                    setFormValues(today, oneMonth, template);
                    break;
                }
            }
        }

        vm.update = function () {
            if (vm.staffRosterParametrization.firstShiftDate != null &&
                vm.staffRosterParametrization.lastShiftDate != null &&
                vm.staffRosterParametrization.planningWindowStart != null &&
                vm.staffRosterParametrization.planningWindowEnd != null
            ) vm.isSaving = false;
            else vm.isSaving = true;
        };

        function setFormValues(firstDate, lastDate, template) {
            var fsd = getDateShift(firstDate);
            var lsd = getClosestDateShift(lastDate);
            var pwsDate = firstDate;
            var pws = getPastClosestDateShift(new Date(pwsDate.setMonth(pwsDate.getMonth() - 1)));
            var pwe = fsd;
            if (fsd == null || lsd == null || pws == null || pwe == null) {
                vm.isSaving = true;
            } else vm.isSaving = false;

            vm.staffRosterParametrization.description = fsd.date + ' - ' + lsd.date;
            vm.staffRosterParametrization.name = template + ' ' + new Date();
            vm.staffRosterParametrization.firstShiftDate = fsd;
            vm.staffRosterParametrization.lastShiftDate = lsd;
            vm.staffRosterParametrization.planningWindowStart = pws;
            vm.staffRosterParametrization.planningWindowEnd = pwe;
        }

        function getDateShift(date) {
            try {
                for (var i = 0; i < vm.shiftdates.length; i++) {
                    var shiftDate = vm.shiftdates[i];
                    if (new Date(date.yyyymmdd()).valueOf() == new Date(shiftDate.date).valueOf()) {
                        return shiftDate;
                        break;
                    }
                }
            } catch (err) {
            }
            return null;
        }

        function getClosestDateShift(date) {
            var latest = vm.shiftdates[0];
            try {
                for (var i = 0; i < vm.shiftdates.length; i++) {
                    var shiftDate = vm.shiftdates[i];
                    if (new Date(date.yyyymmdd()).valueOf() < new Date(shiftDate.date).valueOf()) {
                        return latest;
                        break;
                    } else {
                        latest = shiftDate;
                    }
                }
            } catch (err) {
            }
            return latest;
        }

        function getPastClosestDateShift(date) {
            var latest = vm.shiftdates[0];
            try {
                for (var i = vm.shiftdates.length - 1; i > 0; i--) {
                    var shiftDate = vm.shiftdates[i];
                    if (new Date(date.yyyymmdd()).valueOf() >= new Date(shiftDate.date).valueOf()) {
                        latest = shiftDate;
                        break;
                    }
                }
            } catch (err) {
            }
            return latest;
        }

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
