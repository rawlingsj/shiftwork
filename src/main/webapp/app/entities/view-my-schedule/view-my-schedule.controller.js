var vScope = "";
(function () {
	'use strict';

	angular
	.module('shiftworkApp')
	.controller('ViewMyScheduleController', ViewMyScheduleController);

	ViewMyScheduleController.$inject = ['$scope', 'ViewMySchedule', 'Employee', 'Typeahead', '$filter', 'calendarConfig', '$state'];

	function ViewMyScheduleController($scope, ViewMySchedule, Employee, Typeahead, $filter, calendarConfig, $state) {

		vScope = $scope;
		
		var vm = this;
		vm.calendarConfig = calendarConfig;

		vm.employees = [];
		vm.loadAll = function () {
			Employee.query(function (result) {
				vm.employees = result;
			});
		};
		vm.loadAll();

		vm.resetFilter = function () {
			vm.loadAll();
			delete vm.currentEmployeeId;
			vm.events = new Array();
		};
		
		vm.getEmployee = function (keyword) {
			if (keyword) {
				return Typeahead.findEmployees(keyword);
			} else {
				vm.resetFilter();
				return [];
			}
		};

		vm.onTypeaheadCallback = function ($item, $model, $label) {
			vm.currentEmployeeId = $item.id;
			vm.loadEmployeeSchedule();
		};

		vm.loadEmployeeSchedule = function () {

			vm.events = new Array();

			var monthRange = vm.getMonthDateRange(vm.viewDate.getFullYear(), vm.viewDate.getMonth());
			var startTimeStamp = monthRange.start.toDate().getTime();
			var endTimeStamp = monthRange.end.toDate().getTime();

			/* console.log("vm.currentEmployeeId : ", vm.currentEmployeeId);
			console.log("startTimeStamp : ", startTimeStamp);
			console.log("endTimeStamp : ", endTimeStamp); */

			//vm.employeeSchedule = [{"shiftType":{"id":1046,"code":"D","index":2,"description":"Day shift","nightShift":false,"startTime":"08:30","endTime":"16:30","tasks":null,"endTimeString":"16:30:00","startTimeString":"08:30:00"},"shiftDate":{"id":1048,"dayIndex":14610,"date":"2016-09-01","dayOfWeek":"FRIDAY","dateString":"2016-09-01"},"tasks":[{"id":1031,"code":"T3","description":"Task3","staffNeeded":1,"taskType":"SHORT","importance":"Not_Important","urgency":"Urgent"},{"id":1032,"code":"T4","description":"Task4","staffNeeded":1,"taskType":"MAIN","importance":"Important","urgency":"Not_Urgent"}],"coworkers":[{"id":1393,"code":"I","name":"Employee I"},{"id":1392,"code":"J","name":"Employee J"}]},{"shiftType":{"id":1044,"code":"E","index":0,"description":"Early","nightShift":false,"startTime":"06:30","endTime":"14:30","tasks":null,"endTimeString":"14:30:00","startTimeString":"06:30:00"},"shiftDate":{"id":1060,"dayIndex":14611,"date":"2016-09-02","dayOfWeek":"SATURDAY","dateString":"2016-09-02"},"tasks":[{"id":1029,"code":"T1","description":"Task1","staffNeeded":1,"taskType":"SHORT","importance":"Not_Important","urgency":"Urgent"},{"id":1030,"code":"T2","description":"Task2","staffNeeded":1,"taskType":"MAIN","importance":"Important","urgency":"Urgent"}],"coworkers":[{"id":1388,"code":"D","name":"Employee D"}]},{"shiftType":{"id":1044,"code":"E","index":0,"description":"Early","nightShift":false,"startTime":"06:30","endTime":"14:30","tasks":null,"endTimeString":"14:30:00","startTimeString":"06:30:00"},"shiftDate":{"id":1072,"dayIndex":14612,"date":"2016-09-03","dayOfWeek":"SUNDAY","dateString":"2016-09-03"},"tasks":[{"id":1029,"code":"T1","description":"Task1","staffNeeded":1,"taskType":"SHORT","importance":"Not_Important","urgency":"Urgent"},{"id":1030,"code":"T2","description":"Task2","staffNeeded":1,"taskType":"MAIN","importance":"Important","urgency":"Urgent"}],"coworkers":[{"id":1388,"code":"D","name":"Employee D"}]},{"shiftType":{"id":1044,"code":"E","index":0,"description":"Early","nightShift":false,"startTime":"06:30","endTime":"14:30","tasks":null,"endTimeString":"14:30:00","startTimeString":"06:30:00"},"shiftDate":{"id":1084,"dayIndex":14613,"date":"2016-09-04","dayOfWeek":"MONDAY","dateString":"2016-09-04"},"tasks":[{"id":1029,"code":"T1","description":"Task1","staffNeeded":1,"taskType":"SHORT","importance":"Not_Important","urgency":"Urgent"},{"id":1030,"code":"T2","description":"Task2","staffNeeded":1,"taskType":"MAIN","importance":"Important","urgency":"Urgent"}],"coworkers":[{"id":1388,"code":"D","name":"Employee D"}]},{"shiftType":{"id":1044,"code":"E","index":0,"description":"Early","nightShift":false,"startTime":"06:30","endTime":"14:30","tasks":null,"endTimeString":"14:30:00","startTimeString":"06:30:00"},"shiftDate":{"id":1096,"dayIndex":14614,"date":"2016-09-05","dayOfWeek":"TUESDAY","dateString":"2016-09-05"},"tasks":[{"id":1029,"code":"T1","description":"Task1","staffNeeded":1,"taskType":"SHORT","importance":"Not_Important","urgency":"Urgent"},{"id":1030,"code":"T2","description":"Task2","staffNeeded":1,"taskType":"MAIN","importance":"Important","urgency":"Urgent"}],"coworkers":[{"id":1391,"code":"G","name":"Employee G"}]},{"shiftType":{"id":1044,"code":"E","index":0,"description":"Early","nightShift":false,"startTime":"06:30","endTime":"14:30","tasks":null,"endTimeString":"14:30:00","startTimeString":"06:30:00"},"shiftDate":{"id":1132,"dayIndex":14617,"date":"2016-09-08","dayOfWeek":"FRIDAY","dateString":"2016-09-08"},"tasks":[{"id":1029,"code":"T1","description":"Task1","staffNeeded":1,"taskType":"SHORT","importance":"Not_Important","urgency":"Urgent"},{"id":1030,"code":"T2","description":"Task2","staffNeeded":1,"taskType":"MAIN","importance":"Important","urgency":"Urgent"}],"coworkers":[{"id":1388,"code":"D","name":"Employee D"}]},{"shiftType":{"id":1044,"code":"E","index":0,"description":"Early","nightShift":false,"startTime":"06:30","endTime":"14:30","tasks":null,"endTimeString":"14:30:00","startTimeString":"06:30:00"},"shiftDate":{"id":1144,"dayIndex":14618,"date":"2016-09-09","dayOfWeek":"SATURDAY","dateString":"2016-09-09"},"tasks":[{"id":1029,"code":"T1","description":"Task1","staffNeeded":1,"taskType":"SHORT","importance":"Not_Important","urgency":"Urgent"},{"id":1030,"code":"T2","description":"Task2","staffNeeded":1,"taskType":"MAIN","importance":"Important","urgency":"Urgent"}],"coworkers":[{"id":1388,"code":"D","name":"Employee D"}]},{"shiftType":{"id":1044,"code":"E","index":0,"description":"Early","nightShift":false,"startTime":"06:30","endTime":"14:30","tasks":null,"endTimeString":"14:30:00","startTimeString":"06:30:00"},"shiftDate":{"id":1156,"dayIndex":14619,"date":"2016-09-10","dayOfWeek":"SUNDAY","dateString":"2016-09-10"},"tasks":[{"id":1029,"code":"T1","description":"Task1","staffNeeded":1,"taskType":"SHORT","importance":"Not_Important","urgency":"Urgent"},{"id":1030,"code":"T2","description":"Task2","staffNeeded":1,"taskType":"MAIN","importance":"Important","urgency":"Urgent"}],"coworkers":[{"id":1388,"code":"D","name":"Employee D"}]},{"shiftType":{"id":1044,"code":"E","index":0,"description":"Early","nightShift":false,"startTime":"06:30","endTime":"14:30","tasks":null,"endTimeString":"14:30:00","startTimeString":"06:30:00"},"shiftDate":{"id":1168,"dayIndex":14620,"date":"2016-09-11","dayOfWeek":"MONDAY","dateString":"2016-09-11"},"tasks":[{"id":1029,"code":"T1","description":"Task1","staffNeeded":1,"taskType":"SHORT","importance":"Not_Important","urgency":"Urgent"},{"id":1030,"code":"T2","description":"Task2","staffNeeded":1,"taskType":"MAIN","importance":"Important","urgency":"Urgent"}],"coworkers":[{"id":1394,"code":"J","name":"Employee J"}]},{"shiftType":{"id":1044,"code":"E","index":0,"description":"Early","nightShift":false,"startTime":"06:30","endTime":"14:30","tasks":null,"endTimeString":"14:30:00","startTimeString":"06:30:00"},"shiftDate":{"id":1180,"dayIndex":14621,"date":"2016-09-12","dayOfWeek":"TUESDAY","dateString":"2016-09-12"},"tasks":[{"id":1029,"code":"T1","description":"Task1","staffNeeded":1,"taskType":"SHORT","importance":"Not_Important","urgency":"Urgent"},{"id":1030,"code":"T2","description":"Task2","staffNeeded":1,"taskType":"MAIN","importance":"Important","urgency":"Urgent"}],"coworkers":[{"id":1388,"code":"D","name":"Employee D"}]},{"shiftType":{"id":1044,"code":"E","index":0,"description":"Early","nightShift":false,"startTime":"06:30","endTime":"14:30","tasks":null,"endTimeString":"14:30:00","startTimeString":"06:30:00"},"shiftDate":{"id":1192,"dayIndex":14622,"date":"2016-09-13","dayOfWeek":"WEDNESDAY","dateString":"2016-09-13"},"tasks":[{"id":1029,"code":"T1","description":"Task1","staffNeeded":1,"taskType":"SHORT","importance":"Not_Important","urgency":"Urgent"},{"id":1030,"code":"T2","description":"Task2","staffNeeded":1,"taskType":"MAIN","importance":"Important","urgency":"Urgent"}],"coworkers":[{"id":1388,"code":"D","name":"Employee D"}]},{"shiftType":{"id":1046,"code":"D","index":2,"description":"Day shift","nightShift":false,"startTime":"08:30","endTime":"16:30","tasks":null,"endTimeString":"16:30:00","startTimeString":"08:30:00"},"shiftDate":{"id":1216,"dayIndex":14624,"date":"2016-09-15","dayOfWeek":"FRIDAY","dateString":"2016-09-15"},"tasks":[{"id":1031,"code":"T3","description":"Task3","staffNeeded":1,"taskType":"SHORT","importance":"Not_Important","urgency":"Urgent"},{"id":1032,"code":"T4","description":"Task4","staffNeeded":1,"taskType":"MAIN","importance":"Important","urgency":"Not_Urgent"}],"coworkers":[{"id":1390,"code":"F","name":"Employee F"}]},{"shiftType":{"id":1044,"code":"E","index":0,"description":"Early","nightShift":false,"startTime":"06:30","endTime":"14:30","tasks":null,"endTimeString":"14:30:00","startTimeString":"06:30:00"},"shiftDate":{"id":1228,"dayIndex":14625,"date":"2016-09-16","dayOfWeek":"SATURDAY","dateString":"2016-09-16"},"tasks":[{"id":1029,"code":"T1","description":"Task1","staffNeeded":1,"taskType":"SHORT","importance":"Not_Important","urgency":"Urgent"},{"id":1030,"code":"T2","description":"Task2","staffNeeded":1,"taskType":"MAIN","importance":"Important","urgency":"Urgent"}],"coworkers":[{"id":1388,"code":"D","name":"Employee D"}]},{"shiftType":{"id":1044,"code":"E","index":0,"description":"Early","nightShift":false,"startTime":"06:30","endTime":"14:30","tasks":null,"endTimeString":"14:30:00","startTimeString":"06:30:00"},"shiftDate":{"id":1240,"dayIndex":14626,"date":"2016-09-17","dayOfWeek":"SUNDAY","dateString":"2016-09-17"},"tasks":[{"id":1029,"code":"T1","description":"Task1","staffNeeded":1,"taskType":"SHORT","importance":"Not_Important","urgency":"Urgent"},{"id":1030,"code":"T2","description":"Task2","staffNeeded":1,"taskType":"MAIN","importance":"Important","urgency":"Urgent"}],"coworkers":[{"id":1388,"code":"D","name":"Employee D"}]},{"shiftType":{"id":1044,"code":"E","index":0,"description":"Early","nightShift":false,"startTime":"06:30","endTime":"14:30","tasks":null,"endTimeString":"14:30:00","startTimeString":"06:30:00"},"shiftDate":{"id":1252,"dayIndex":14627,"date":"2016-09-18","dayOfWeek":"MONDAY","dateString":"2016-09-18"},"tasks":[{"id":1029,"code":"T1","description":"Task1","staffNeeded":1,"taskType":"SHORT","importance":"Not_Important","urgency":"Urgent"},{"id":1030,"code":"T2","description":"Task2","staffNeeded":1,"taskType":"MAIN","importance":"Important","urgency":"Urgent"}],"coworkers":[{"id":1391,"code":"G","name":"Employee G"}]},{"shiftType":{"id":1044,"code":"E","index":0,"description":"Early","nightShift":false,"startTime":"06:30","endTime":"14:30","tasks":null,"endTimeString":"14:30:00","startTimeString":"06:30:00"},"shiftDate":{"id":1288,"dayIndex":14630,"date":"2016-09-21","dayOfWeek":"THURSDAY","dateString":"2016-09-21"},"tasks":[{"id":1029,"code":"T1","description":"Task1","staffNeeded":1,"taskType":"SHORT","importance":"Not_Important","urgency":"Urgent"},{"id":1030,"code":"T2","description":"Task2","staffNeeded":1,"taskType":"MAIN","importance":"Important","urgency":"Urgent"}],"coworkers":[{"id":1391,"code":"G","name":"Employee G"}]},{"shiftType":{"id":1044,"code":"E","index":0,"description":"Early","nightShift":false,"startTime":"06:30","endTime":"14:30","tasks":null,"endTimeString":"14:30:00","startTimeString":"06:30:00"},"shiftDate":{"id":1300,"dayIndex":14631,"date":"2016-09-22","dayOfWeek":"FRIDAY","dateString":"2016-09-22"},"tasks":[{"id":1029,"code":"T1","description":"Task1","staffNeeded":1,"taskType":"SHORT","importance":"Not_Important","urgency":"Urgent"},{"id":1030,"code":"T2","description":"Task2","staffNeeded":1,"taskType":"MAIN","importance":"Important","urgency":"Urgent"}],"coworkers":[{"id":1388,"code":"D","name":"Employee D"}]},{"shiftType":{"id":1044,"code":"E","index":0,"description":"Early","nightShift":false,"startTime":"06:30","endTime":"14:30","tasks":null,"endTimeString":"14:30:00","startTimeString":"06:30:00"},"shiftDate":{"id":1312,"dayIndex":14632,"date":"2016-09-23","dayOfWeek":"SATURDAY","dateString":"2016-09-23"},"tasks":[{"id":1029,"code":"T1","description":"Task1","staffNeeded":1,"taskType":"SHORT","importance":"Not_Important","urgency":"Urgent"},{"id":1030,"code":"T2","description":"Task2","staffNeeded":1,"taskType":"MAIN","importance":"Important","urgency":"Urgent"}],"coworkers":[{"id":1388,"code":"D","name":"Employee D"}]},{"shiftType":{"id":1044,"code":"E","index":0,"description":"Early","nightShift":false,"startTime":"06:30","endTime":"14:30","tasks":null,"endTimeString":"14:30:00","startTimeString":"06:30:00"},"shiftDate":{"id":1324,"dayIndex":14633,"date":"2016-09-24","dayOfWeek":"SUNDAY","dateString":"2016-09-24"},"tasks":[{"id":1029,"code":"T1","description":"Task1","staffNeeded":1,"taskType":"SHORT","importance":"Not_Important","urgency":"Urgent"},{"id":1030,"code":"T2","description":"Task2","staffNeeded":1,"taskType":"MAIN","importance":"Important","urgency":"Urgent"}],"coworkers":[{"id":1388,"code":"D","name":"Employee D"}]},{"shiftType":{"id":1044,"code":"E","index":0,"description":"Early","nightShift":false,"startTime":"06:30","endTime":"14:30","tasks":null,"endTimeString":"14:30:00","startTimeString":"06:30:00"},"shiftDate":{"id":1336,"dayIndex":14634,"date":"2016-09-25","dayOfWeek":"MONDAY","dateString":"2016-09-25"},"tasks":[{"id":1029,"code":"T1","description":"Task1","staffNeeded":1,"taskType":"SHORT","importance":"Not_Important","urgency":"Urgent"},{"id":1030,"code":"T2","description":"Task2","staffNeeded":1,"taskType":"MAIN","importance":"Important","urgency":"Urgent"}],"coworkers":[{"id":1388,"code":"D","name":"Employee D"}]},{"shiftType":{"id":1044,"code":"E","index":0,"description":"Early","nightShift":false,"startTime":"06:30","endTime":"14:30","tasks":null,"endTimeString":"14:30:00","startTimeString":"06:30:00"},"shiftDate":{"id":1348,"dayIndex":14635,"date":"2016-09-26","dayOfWeek":"TUESDAY","dateString":"2016-09-26"},"tasks":[{"id":1029,"code":"T1","description":"Task1","staffNeeded":1,"taskType":"SHORT","importance":"Not_Important","urgency":"Urgent"},{"id":1030,"code":"T2","description":"Task2","staffNeeded":1,"taskType":"MAIN","importance":"Important","urgency":"Urgent"}],"coworkers":[{"id":1388,"code":"D","name":"Employee D"}]},{"shiftType":{"id":1044,"code":"E","index":0,"description":"Early","nightShift":false,"startTime":"06:30","endTime":"14:30","tasks":null,"endTimeString":"14:30:00","startTimeString":"06:30:00"},"shiftDate":{"id":1360,"dayIndex":14636,"date":"2016-09-27","dayOfWeek":"WEDNESDAY","dateString":"2016-09-27"},"tasks":[{"id":1029,"code":"T1","description":"Task1","staffNeeded":1,"taskType":"SHORT","importance":"Not_Important","urgency":"Urgent"},{"id":1030,"code":"T2","description":"Task2","staffNeeded":1,"taskType":"MAIN","importance":"Important","urgency":"Urgent"}],"coworkers":[{"id":1391,"code":"G","name":"Employee G"}]},{"shiftType":{"id":1044,"code":"E","index":0,"description":"Early","nightShift":false,"startTime":"06:30","endTime":"14:30","tasks":null,"endTimeString":"14:30:00","startTimeString":"06:30:00"},"shiftDate":{"id":1372,"dayIndex":14637,"date":"2016-09-28","dayOfWeek":"THURSDAY","dateString":"2016-09-28"},"tasks":[{"id":1029,"code":"T1","description":"Task1","staffNeeded":1,"taskType":"SHORT","importance":"Not_Important","urgency":"Urgent"},{"id":1030,"code":"T2","description":"Task2","staffNeeded":1,"taskType":"MAIN","importance":"Important","urgency":"Urgent"}],"coworkers":[{"id":1394,"code":"J","name":"Employee J"}]}];
			//vm.formatEmployeeSchedule();
			ViewMySchedule.query({
				employee : vm.currentEmployeeId,
				from : startTimeStamp,
				to : endTimeStamp
			},
			function (result) {
				vm.employeeSchedule = result;
				vm.formatEmployeeSchedule();
			});
		};

		vm.formatEmployeeSchedule = function () {

			for (var index = 0; index < vm.employeeSchedule.length; index++) {

				var empTasks = vm.employeeSchedule[index].tasks;
				var shiftDate = vm.employeeSchedule[index].shiftDate;

				for (var taskIndex = 0; taskIndex < empTasks.length; taskIndex++) {

					var empTask = empTasks[taskIndex];
					var taskImportance = empTask.importance.toLowerCase() == "important" ? calendarConfig.colorTypes.important : calendarConfig.colorTypes.info;

					vm.events.push({
						$id : taskIndex + 1,
						title : empTask.description,
						startsAt : new Date(shiftDate.date),
						endsAt : new Date(shiftDate.date),
						color : taskImportance,
						data : {
							empShift : vm.employeeSchedule[index],
							taskIndex : taskIndex
						}
					});
				}

			}

		};

		vm.calendarView = 'month';
		vm.viewDate = new Date();
		vm.isCellOpen = true;

		vm.toggle = function ($event, field, event) {
			$event.preventDefault();
			$event.stopPropagation();
			event[field] = !event[field];
		};

		vm.viewDateChanged = function () {
			//console.log("viewDateChanged", vm.viewDate);
			vm.loadEmployeeSchedule();
		};

		vm.getMonthDateRange = function (year, month) {
			var startDate = moment([year, month]);
			var endDate = moment(startDate).endOf('month');
			
			// make sure to call toDate() for plain JavaScript date type
			return {
				start : startDate,
				end : endDate
			};
		};

		vm.showTaskDetail = function (taskDetail) {
			//console.log("taskDetail", taskDetail);
			$state.go("view-my-schedule-detail", taskDetail);
		};
	}
})();