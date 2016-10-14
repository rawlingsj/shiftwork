(function () {
	'use strict';

	angular
	.module('shiftworkApp')
	.controller('ViewMyScheduleController', ViewMyScheduleController);

	ViewMyScheduleController.$inject = ['$scope', 'ViewMySchedule', 'Employee', 'Typeahead', '$filter', 'calendarConfig', '$state', '$translate'];

	function ViewMyScheduleController($scope, ViewMySchedule, Employee, Typeahead, $filter, calendarConfig, $state, $translate) {
		
		var vm = this;
		vm.calendarView = 'month';
		
		calendarConfig.templates.calendarMonthCell = 'app/entities/view-my-schedule/calendarMonthCell.html';
		calendarConfig.templates.calendarSlideBox = 'app/entities/view-my-schedule/calendarSlideBox.html';
		calendarConfig.templates.calendarMonthCellEvents = 'app/entities/view-my-schedule/calendarMonthCellEvents.html';

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
			var shiftViewDuration = vm.getShiftViewDuration();
		
			/* console.log("start:: " + new Date(shiftViewDuration.startTimeStamp));
			console.log("end:: " + new Date(shiftViewDuration.endTimeStamp)); */ 

			ViewMySchedule.query({
				employee : vm.currentEmployeeId,
				fromDate : shiftViewDuration.startTimeStamp,
				toDate : shiftViewDuration.endTimeStamp
			},
			function (result) {
				vm.employeeSchedule = result;
				vm.formatEmployeeSchedule();
			});
		};
		
		vm.getShiftViewDuration = function() {
			
			var startTimeStamp = -1;
			var endTimeStamp = -1;
			
			if(vm.calendarView === 'day') {
				var dayRange = vm.getDateRange(vm.viewDate, 'day');
				startTimeStamp = dayRange.start.toDate().getTime();
				endTimeStamp = dayRange.end.toDate().getTime();
			}
			else if(vm.calendarView === 'week') {
				var weekRange = vm.getDateRange(vm.viewDate, 'week');
				startTimeStamp = weekRange.start.toDate().getTime();
				endTimeStamp = weekRange.end.toDate().getTime();
			}
			else if(vm.calendarView === 'month') {
				var monthRange = vm.getDateRange(vm.viewDate, 'month');
				startTimeStamp = monthRange.start.toDate().getTime();
				endTimeStamp = monthRange.end.toDate().getTime();
			}
			else if(vm.calendarView === 'year') {
				var yearRange = vm.getDateRange(vm.viewDate, 'year');
				startTimeStamp = yearRange.start.toDate().getTime();
				endTimeStamp = yearRange.end.toDate().getTime();
			}
			
			return {
				startTimeStamp : startTimeStamp,
				endTimeStamp : endTimeStamp
			};
		};

		vm.formatEmployeeSchedule = function () {

			for (var index = 0; index < vm.employeeSchedule.length; index++) {

				var empTasks = vm.employeeSchedule[index].tasks;
				var shiftStartDate = new Date(vm.employeeSchedule[index].shiftDate.date);
				var shiftEndDate = new Date(vm.employeeSchedule[index].shiftDate.date);
				var shiftType = vm.employeeSchedule[index].shiftType;
				
				var shiftStartHour = shiftType.startTime.split(":")[0];
				var shiftStartMinutes = shiftType.startTime.split(":")[1];
				var shiftEndHour = shiftType.endTime.split(":")[0];
				var shiftEndMinutes = shiftType.endTime.split(":")[1];

				/*if (shiftEndHour < shiftStartHour) {
					shiftEndDate.setDate(shiftStartDate.getDate() + 1);
				}*/ 

				var shiftStartDateTime = new Date((new Date(shiftStartDate.setHours(shiftStartHour))).setMinutes(shiftStartMinutes));
				var shiftEndDateTime = new Date((new Date(shiftEndDate.setHours(shiftEndHour))).setMinutes(shiftEndMinutes));
				
				var shiftDescription = $translate.instant("shiftworkApp.viewMySchedule.shiftDescription")
				.replace("##_DATE_##", new moment(new Date(shiftEndDate)).format('MMMM DD'))
				.replace("##_SHIFT_TYPE_##", shiftType.description);

				var taskDescription = "";
				
				for (var taskIndex = 0; taskIndex < empTasks.length; taskIndex++) {

					taskDescription += "<li class='taskDescription'><strong>" + empTasks[taskIndex].description + "</strong>";
					
					if(empTasks[taskIndex].coworkers.length > 0) {
						
						var coworkers = empTasks[taskIndex].coworkers.map(function(coworker){return coworker.name});
					
						if(coworkers.length > 1) {
							coworkers = coworkers.join(",");
							var lastCommaIndex = coworkers.lastIndexOf(",");
							coworkers = coworkers.substr(0, lastCommaIndex) + " & " + coworkers.substr(lastCommaIndex + 1);
						}
						else {
							coworkers = coworkers.join("");
						}
							
						taskDescription += ", " + $translate.instant("shiftworkApp.viewMySchedule.taskDescription")
						.replace("##_CO_WORKERS_##", coworkers);
					
					}
					
					taskDescription += "</li>";
						
				}
				
				shiftDescription += taskDescription;
				
				vm.events.push({
					$id : index + 1,
					title : shiftDescription,
					startsAt : new Date(shiftStartDateTime),
					endsAt : new Date(shiftEndDateTime),
					color : calendarConfig.colorTypes.info,
					data : {
						empShift : vm.employeeSchedule[index]
					},
					cssClass : "shiftDescription",
					allDay: false
				});
				
			}

		};

		vm.viewDate = new Date();
		vm.isCellOpen = true;

		vm.toggle = function ($event, field, event) {
			$event.preventDefault();
			$event.stopPropagation();
			event[field] = !event[field];
		};

		vm.viewDateChanged = function () {
			vm.loadEmployeeSchedule();
		};

		/*	getDatRange
			date - Date Object, 
			span - 'day', 'week', 'month' or 'year' 
		*/
		vm.getDateRange = function (date, span) { 
			
			var currentDate = moment(date);
			var startDate =  moment(currentDate).startOf(span);
			var endDate = moment(currentDate).endOf(span);
			
			// make sure to call toDate() for plain JavaScript date type
			return {
				start : startDate,
				end : endDate
			};
		};
		
		vm.cellModifier = function (cell) {
			if(cell.events && cell.events.length > 0) {
				var empShift = cell.events[0].data.empShift;
				var isNightShift = empShift.shiftType.nightShift;
				
				cell.cssClass = isNightShift ? 'night-shift' : 'day-shift';
			}
		};
		
		$scope.$on('$destroy', function() {
			calendarConfig.templates.calendarMonthCell = 'mwl/calendarMonthCell.html';
			calendarConfig.templates.calendarSlideBox = 'mwl/calendarSlideBox.html';
			calendarConfig.templates.calendarMonthCellEvents = 'mwl/calendarMonthCellEvents.html';
		});
	}
})();