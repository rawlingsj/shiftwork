(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('EmployeeController', EmployeeController);

    EmployeeController.$inject = ['$scope', '$state', 'Employee','SearchFields'];

    function EmployeeController ($scope, $state, Employee,SearchFields) {
    	
        var vm = this;
        vm.employees = [];
        vm.loadAll = function() {
            Employee.query(function(result) {
                vm.employees = result;
            });
        };
        
       
        vm.getEmployee=function(keyword){
        	SearchFields.search({value:keyword},function(result){
        		vm.employees = result;
        	})
        	return vm.employees;
        },
         vm.ngModelOptionsSelected = function(value) {
			if (arguments.length) {
				_selected = value;
			} else {
				return _selected;
			}
		};

		vm.modelOptions = {
			debounce : {
				'default' : 500,
				blur : 250
			},
			getterSetter : true
		};
        
        vm.onTypeaheadCallback = function($item, $model, $label){
        	SearchFields.search({value:$item.name},function(result){
        		vm.employees = result;
        	})
        	return vm.employees;
        },
        vm.loadAll();
        
    }
})();
