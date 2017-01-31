(function () {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('ShiftAssignmentController', ShiftAssignmentController);

    ShiftAssignmentController.$inject = ['$scope', '$state', 'ShiftAssignment', 'ShiftDate', '$http'];

    function ShiftAssignmentController($scope, $state, ShiftAssignment, ShiftDate, $http) {
        var vm = this;
        vm.shiftAssignments = [];
        vm.shiftdates = ShiftDate.query();

        vm.loadAll = function () {
            ShiftAssignment.query(function (result) {
                vm.shiftAssignments = result;
            });
        };

        vm.loadAll();

        vm.exportToExcel = function (shiftDate) {
            $http({
                url: '/api/schedules',
                params: {
                    'shiftDate': shiftDate.id
                },
                method: 'GET',
                responseType: 'arraybuffer',
                headers: {
                    'Accept': 'application/vnd.ms-excel'
                }
            }).success(function (data) {
                var blob = new Blob([data], {
                    type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
                });
                saveAs(blob, 'Schedule-' + shiftDate.dateString + '.xlsx');
            }).error(function () {
                console.error('Unabale to get excel spreadsheet');
            });
        };
    }
})();
