(function () {
	'use strict';

	angular
	.module('shiftworkApp')
	.controller('EmployeeAgreementController', EmployeeAgreementController);

	EmployeeAgreementController.$inject = ['$scope', '$state', 'EmployeeAgreement'];

	function EmployeeAgreementController($scope, $state, EmployeeAgreement) {
		
		var vm = this;
		vm.employeeAgreements = [];
		vm.loadAll = function () {
			EmployeeAgreement.query(function (result) {
				vm.employeeAgreements = result;
			});
		};

		vm.loadAll();

	}
})();