(function () {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('EmployeeLeaveAbsenceController', EmployeeLeaveAbsenceController);

    EmployeeLeaveAbsenceController.$inject = ['$scope', '$state', 'EmployeeLeaveAbsence', 'Typeahead', '$filter'];

    function EmployeeLeaveAbsenceController($scope, $state, EmployeeLeaveAbsence, Typeahead, $filter) {
        var vm = this;
        vm.employeeLeaveAbsences = [];
        vm.loadAll = function () {
            EmployeeLeaveAbsence.query(function (result) {
                vm.employeeLeaveAbsences = result;
            });
        };

        vm.resetFilter = function () {
            vm.loadAll();
        };

        vm.getEmployee = function (keyword) {
            if (keyword) {
                return Typeahead.findEmployees(keyword);
            } else {
                vm.resetFilter();
                return [];
            }
        };

        vm.ngModelOptionsSelected = function (value) {
            if (arguments.length) {
                var _selected = value;
            } else {
                return _selected;
            }
        };

        vm.modelOptions = {
            debounce: {
                'default': 500,
                'blur': 250
            },
            getterSetter: true
        };

        vm.onTypeaheadCallback = function ($item, $model, $label) {
            EmployeeLeaveAbsence.query({employee: $item.id}, function (result) {
                vm.employeeLeaveAbsences = result;
            });
        };

        vm.loadAll();
    }
})();
