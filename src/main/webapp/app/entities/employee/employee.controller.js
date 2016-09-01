(function () {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('EmployeeController', EmployeeController);

    EmployeeController.$inject = ['$scope', '$state', 'Employee', 'SearchFields', '$filter'];

    function EmployeeController($scope, $state, Employee, SearchFields, $filter) {
        var vm = this;
        vm.employees = [];

        vm.loadAll = function () {
            Employee.query(function (result) {
                vm.employees = result;
            });
        };

        vm.resetFilter = function () {
            vm.loadAll();
        };

        vm.getEmployee = function (keyword) {
            if (keyword) {
                return SearchFields.search({value: keyword}).$promise;
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
            vm.employees = $filter('filter')(vm.employees, {id: $item.id});
        };

        vm.loadAll();
    }
})();
