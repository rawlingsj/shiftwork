(function () {
	'use strict';

	angular
	.module('shiftworkApp')
	.controller('ViewMyScheduleDetailController', ViewMyScheduleDetailController);

	ViewMyScheduleDetailController.$inject = ['$scope', '$stateParams', '$uibModalInstance'];

	function ViewMyScheduleDetailController($scope, $stateParams, $uibModalInstance) {

		var vm = this;

		vm.shiftDetails = $stateParams.data.empShift;
		vm.taskDetails = vm.shiftDetails.tasks[$stateParams.data.taskIndex];

		vm.clear = function () {
			$uibModalInstance.dismiss('cancel');
		};

	}
})();