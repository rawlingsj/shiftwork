(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('ViewDayPlanController', ViewDayPlanController);

    ViewDayPlanController.$inject = ['$scope', '$state', 'ViewDayPlan', '$translate'];

    function ViewDayPlanController ($scope, $state, ViewDayPlan, $translate) {
		
        var vm = this;
		vm.loadAll = function() {
			var timestamp = Date.now();
            ViewDayPlan.query(
			{
				fromShiftDate : timestamp,
				toShiftDate : timestamp
			}
			,function(result) {
				//vm.shiftAssignments = result;
				
				// TODO - Hard coded data - remove
                vm.shiftAssignments = [
					{"employee":{"code":"101","contract":{"code":"string","contractLineList":[{"contractLineType":"SINGLE_ASSIGNMENT_PER_DAY","id":0}],"description":"string","id":0,"weekendDefinition":{"days":["MONDAY"],"description":"string","id":0}},"id":0,"isSick":true,"name":"Jane Smith","unavailableShiftDate":{"date":"2016-08-29","dayIndex":0,"dayOfWeek":"MONDAY","id":0},"unavailableShiftType":{"code":"string","description":"string","endTime":"string","id":0,"index":0,"nightShift":true,"startTime":"string","tasks":[{"code":"string","description":"string","id":0,"importance":"Important","staffNeeded":0,"taskType":"SHORT","urgency":"Urgent"}]}},"id":0,"indexInShift":0,"isDropped":true,"isNeedToReassign":true,"locked":true,"shift":{"id":0,"index":0,"shiftDate":{"date":"2016-08-29","dayIndex":0,"dayOfWeek":"MONDAY","id":0},"shiftType":{"code":"string","description":"string","endTime":"string","id":0,"index":0,"nightShift":true,"startTime":"string","tasks":[{"code":"string","description":"string","id":0,"importance":"Important","staffNeeded":0,"taskType":"SHORT","urgency":"Urgent"}]},"staffRequired":0},"taskList":[{"code":"string","description":"string","id":0,"importance":"Important","staffNeeded":0,"taskType":"SHORT","urgency":"Urgent"}]},
					{"employee":{"code":"102","contract":{"code":"string","contractLineList":[{"contractLineType":"SINGLE_ASSIGNMENT_PER_DAY","id":0}],"description":"string","id":0,"weekendDefinition":{"days":["MONDAY"],"description":"string","id":0}},"id":0,"isSick":true,"name":"John Doe","unavailableShiftDate":{"date":"2016-08-29","dayIndex":0,"dayOfWeek":"MONDAY","id":0},"unavailableShiftType":{"code":"string","description":"string","endTime":"string","id":0,"index":0,"nightShift":true,"startTime":"string","tasks":[{"code":"string","description":"string","id":0,"importance":"Important","staffNeeded":0,"taskType":"SHORT","urgency":"Urgent"}]}},"id":0,"indexInShift":0,"isDropped":true,"isNeedToReassign":true,"locked":true,"shift":{"id":0,"index":0,"shiftDate":{"date":"2016-08-29","dayIndex":0,"dayOfWeek":"MONDAY","id":0},"shiftType":{"code":"string","description":"string","endTime":"string","id":0,"index":0,"nightShift":true,"startTime":"string","tasks":[{"code":"string","description":"string","id":0,"importance":"Important","staffNeeded":0,"taskType":"SHORT","urgency":"Urgent"}]},"staffRequired":0},"taskList":[{"code":"string","description":"string","id":0,"importance":"Important","staffNeeded":0,"taskType":"SHORT","urgency":"Urgent"}]},
					{"employee":{"code":"103","contract":{"code":"string","contractLineList":[{"contractLineType":"SINGLE_ASSIGNMENT_PER_DAY","id":0}],"description":"string","id":0,"weekendDefinition":{"days":["MONDAY"],"description":"string","id":0}},"id":0,"isSick":true,"name":"Max PI","unavailableShiftDate":{"date":"2016-08-29","dayIndex":0,"dayOfWeek":"MONDAY","id":0},"unavailableShiftType":{"code":"string","description":"string","endTime":"string","id":0,"index":0,"nightShift":true,"startTime":"string","tasks":[{"code":"string","description":"string","id":0,"importance":"Important","staffNeeded":0,"taskType":"SHORT","urgency":"Urgent"}]}},"id":0,"indexInShift":0,"isDropped":true,"isNeedToReassign":true,"locked":true,"shift":{"id":0,"index":0,"shiftDate":{"date":"2016-08-29","dayIndex":0,"dayOfWeek":"MONDAY","id":0},"shiftType":{"code":"string","description":"string","endTime":"string","id":0,"index":0,"nightShift":true,"startTime":"string","tasks":[{"code":"string","description":"string","id":0,"importance":"Important","staffNeeded":0,"taskType":"SHORT","urgency":"Urgent"}]},"staffRequired":0},"taskList":[{"code":"string","description":"string","id":0,"importance":"Important","staffNeeded":0,"taskType":"SHORT","urgency":"Urgent"}]},
					{"employee":{"code":"104","contract":{"code":"string","contractLineList":[{"contractLineType":"SINGLE_ASSIGNMENT_PER_DAY","id":0}],"description":"string","id":0,"weekendDefinition":{"days":["MONDAY"],"description":"string","id":0}},"id":0,"isSick":true,"name":"Jill Doe","unavailableShiftDate":{"date":"2016-08-29","dayIndex":0,"dayOfWeek":"MONDAY","id":0},"unavailableShiftType":{"code":"string","description":"string","endTime":"string","id":0,"index":0,"nightShift":true,"startTime":"string","tasks":[{"code":"string","description":"string","id":0,"importance":"Important","staffNeeded":0,"taskType":"SHORT","urgency":"Urgent"}]}},"id":0,"indexInShift":0,"isDropped":true,"isNeedToReassign":true,"locked":true,"shift":{"id":0,"index":0,"shiftDate":{"date":"2016-08-29","dayIndex":0,"dayOfWeek":"MONDAY","id":0},"shiftType":{"code":"string","description":"string","endTime":"string","id":0,"index":0,"nightShift":true,"startTime":"string","tasks":[{"code":"string","description":"string","id":0,"importance":"Important","staffNeeded":0,"taskType":"SHORT","urgency":"Urgent"}]},"staffRequired":0},"taskList":[{"code":"string","description":"string","id":0,"importance":"Important","staffNeeded":0,"taskType":"SHORT","urgency":"Urgent"}]},
					{"employee":{"code":"105","contract":{"code":"string","contractLineList":[{"contractLineType":"SINGLE_ASSIGNMENT_PER_DAY","id":0}],"description":"string","id":0,"weekendDefinition":{"days":["MONDAY"],"description":"string","id":0}},"id":0,"isSick":true,"name":"John Smith","unavailableShiftDate":{"date":"2016-08-29","dayIndex":0,"dayOfWeek":"MONDAY","id":0},"unavailableShiftType":{"code":"string","description":"string","endTime":"string","id":0,"index":0,"nightShift":true,"startTime":"string","tasks":[{"code":"string","description":"string","id":0,"importance":"Important","staffNeeded":0,"taskType":"SHORT","urgency":"Urgent"}]}},"id":0,"indexInShift":0,"isDropped":true,"isNeedToReassign":true,"locked":true,"shift":{"id":0,"index":0,"shiftDate":{"date":"2016-08-29","dayIndex":0,"dayOfWeek":"MONDAY","id":0},"shiftType":{"code":"string","description":"string","endTime":"string","id":0,"index":0,"nightShift":true,"startTime":"string","tasks":[{"code":"string","description":"string","id":0,"importance":"Important","staffNeeded":0,"taskType":"SHORT","urgency":"Urgent"}]},"staffRequired":0},"taskList":[{"code":"string","description":"string","id":0,"importance":"Important","staffNeeded":0,"taskType":"SHORT","urgency":"Urgent"}]}
				];
            });
        };
        vm.loadAll();
		
		// Update moment locale to display (Yesterday, Today And Tomorrow)
		moment.updateLocale('yesterday-today-tomorrow', {
			calendar : {
				lastDay : '[' + $translate.instant("global.day.yesterday") + ']',
				sameDay : '[' + $translate.instant("global.day.today") + ']',
				nextDay : '[' + $translate.instant("global.day.tomorrow") + ']'
			}
		})
	
		vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.date = false;
        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
		
		vm.shiftDate = new Date();
		vm.todayMoment = moment(new Date());
		vm.shiftDateFormatted = moment(vm.shiftDate).calendar();
		
		vm.shiftDateChanged = function() {
			if(!vm.shiftDate) { // handle clear button
				vm.shiftDate = new Date();
			}
			
			var daysDiff = moment(vm.shiftDate).diff(vm.todayMoment, 'days');

			if (Math.abs(daysDiff) <= 1) { // -1, 0 or 1 (Yesterday, Today or Tomorrow)
				vm.shiftDateFormatted = moment(vm.shiftDate).calendar();
			}
			else {
				vm.shiftDateFormatted = moment(vm.shiftDate).format("YYYY-MM-DD");
			}
		}
    }
})();
