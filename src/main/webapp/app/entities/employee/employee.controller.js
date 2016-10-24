(function () {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('EmployeeController', EmployeeController);

    EmployeeController.$inject = ['$scope', '$state', 'Employee', 'Typeahead', '$filter'];

    function EmployeeController($scope, $state, Employee, Typeahead, $filter) {
        var vm = this;
        vm.employees = [];

        vm.loadAll = function () {
            Employee.query(function (result) {
                vm.employees = result;
            });
        };

        vm.resetFilter = function () {
			console.log("In resetFilter");
            vm.loadAll();
        };

        vm.getEmployee = function (keyword) {
			console.log("In getEmployee");
            if (keyword) {
                return Typeahead.findEmployees(keyword);
            } else {
                vm.resetFilter();
                return [];
            }
        };

        vm.ngModelOptionsSelected = function (value) {
			console.log("In ngModelOptionsSelected");
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
			console.log("In onTypeaheadCallback");
            vm.employees = $filter('filter')(vm.employees, {id: $item.id});
        };

        vm.loadAll();
    }
})();
