var vScope = "";

(function () {
	'use strict';

	angular
	.module('shiftworkApp')
	.controller('ViewMyScheduleController', ViewMyScheduleController);

	ViewMyScheduleController.$inject = ['$scope', 'ViewMySchedule'];

	function ViewMyScheduleController($scope, ViewMySchedule) {
		
		console.log("In ViewMyScheduleController");
	}
})();