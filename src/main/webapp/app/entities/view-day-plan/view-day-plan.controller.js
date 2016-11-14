(function () {
	'use strict';

	angular
	.module('shiftworkApp')
	.controller('ViewDayPlanController', ViewDayPlanController);

	ViewDayPlanController.$inject = ['$scope', '$state', 'ViewDayPlan', '$translate', '$timeout'];

	function ViewDayPlanController($scope, $state, ViewDayPlan, $translate, $timeout) {

		var vm = this;

		vm.shiftAssignments = new Array();
		vm.loadShiftAssignments = function (shiftDay) {
			ViewDayPlan.query({
				shiftDate : shiftDay
			}, function (result) {
				vm.shiftAssignments = result;
				$timeout(function () {
					vm.formatShiftDataForTimeline();
					vm.generateTimeLine();
				});
			});
		};

		vm.loadShiftAssignments(Date.now());

		moment.updateLocale('yesterday-today-tomorrow', { // Update moment locale to display (Yesterday, Today And Tomorrow)
			calendar : {
				lastDay : '[' + $translate.instant("global.day.yesterday") + ']',
				sameDay : '[' + $translate.instant("global.day.today") + ']',
				nextDay : '[' + $translate.instant("global.day.tomorrow") + ']'
			}
		});

		vm.datePickerOpenStatus = {};
		vm.datePickerOpenStatus.date = false;
		vm.openCalendar = function (date) {
			vm.datePickerOpenStatus[date] = true;
		};

		vm.shiftDate = new Date();
		vm.todayMoment = moment(new Date());
		vm.shiftDateFormatted = moment(vm.shiftDate).calendar();

		vm.shiftDateChanged = function () {

			if (!vm.shiftDate) { // handle clear button
				vm.shiftDate = new Date();
			}

			var daysDiff = moment(vm.shiftDate).diff(vm.todayMoment, 'days');

			if (Math.abs(daysDiff) <= 1) { // -1, 0 or 1 (Yesterday, Today or Tomorrow)
				vm.shiftDateFormatted = moment(vm.shiftDate).calendar();
			} else {
				vm.shiftDateFormatted = moment(vm.shiftDate).format("YYYY-MM-DD");
			}

			vm.loadShiftAssignments(vm.shiftDate.getTime());

		};

		vm.generateTimeLine = function () {

			var parentElem = ".well";
			var chartElem = d3.select("#shiftTimeline");
			chartElem.html(""); // Remove previous timeline if any
			var parentWidth = $(parentElem).width();

			var chart = d3.timeline()
				.beginning(vm.minShiftTime) //adding beginning and ending times to speed up rendering a little
				.ending(vm.maxShiftTime)
				.showTimeAxisTick()
				.tickFormat({
					format : d3.time.format("%H"),
					tickTime : d3.time.hours,
					tickInterval : 1,
					tickSize : 4,
					tickValues : null
				})
				.width(parentWidth * 1.5)
				.stack()
				.margin({
					left : 100,
					right : 30,
					top : 0,
					bottom : 0
				})
				.click(function (d, i, datum) { // same way you can bind hover event, same parameters.
					console.log(datum.label);
				})
				.hover(function (d, i, datum) {
					var taskStartTime = new Date(d.starting_time);
					var taskEndTime = new Date(d.ending_time);
					var taskTimings = [moment(taskStartTime).format("H:mm"), moment(taskEndTime).format("H:mm")];
					var div = $('#hoverRes');
					var colors = chart.colors();
					div.find('.coloredDiv').css('background-color', colors(i));
					div.find('#taskDescription').html($translate.instant("shiftworkApp.viewDayPlan.taskName") + ": " + d.label + " <br/>" +
						$translate.instant("shiftworkApp.viewDayPlan.taskTiming") + ": " + taskTimings[0] + " " + $translate.instant("shiftworkApp.viewDayPlan.to") + " " + taskTimings[1]);
				});

			/* var viewBoxHeight = 450;
			if(vm.timelineData.length < 10) {
				viewBoxHeight = vm.timelineData.length * 45;
			} */
			
			var svg = chartElem
				.append("div")
				.classed("svg-container", true)
				.append("svg")
				.attr("width", parentWidth)
				.attr("preserveAspectRatio", "xMinYMin meet")
				//.attr("viewBox", "0 0 " + (parentWidth) + " " + (viewBoxHeight))
				.datum(vm.timelineData)
				.call(chart);

			d3.select("svg").attr("viewBox", "0 0 " + (parentWidth) + " " + d3.select("svg").attr("height"));
			
			//d3.select("svg").classed("svg-content-responsive", true);

			var aspectRatio = d3.select("svg").attr("height") / d3.select("svg").attr("width");

			d3.select(window)
			.on("resize", function () {
				var parentWidth = $(parentElem).width();
				d3.select("svg").attr("width", parentWidth).attr("height", parentWidth * aspectRatio);
			});

		};

		vm.formatShiftDataForTimeline = function () {

			vm.timelineData = new Array();
			var dateObjects = new Array();

			for (var index = 0; index < vm.shiftAssignments.length; index++) {

				var empShiftData = {};
				empShiftData.label = vm.shiftAssignments[index].employee.name;
				empShiftData.class = "shift_" + (index + 1);
				empShiftData.times = new Array();

				var empTasks = vm.shiftAssignments[index].taskList;
				var shiftType = vm.shiftAssignments[index].shift.shiftType;
				var shiftStartDate = new Date(vm.shiftAssignments[index].shift.shiftDate.date);
				var shiftEndDate = new Date(vm.shiftAssignments[index].shift.shiftDate.date);

				var shiftStartHour = shiftType.startTime.split(":")[0];
				var shiftStartMinutes = shiftType.startTime.split(":")[1];
				var shiftEndHour = shiftType.endTime.split(":")[0];
				var shiftEndMinutes = shiftType.endTime.split(":")[1];

				if (shiftEndHour < shiftStartHour) {
					shiftEndDate.setDate(shiftStartDate.getDate() + 1);
				}

				var shiftStartDateTime = new Date((new Date(shiftStartDate.setHours(shiftStartHour))).setMinutes(shiftStartMinutes));
				var shiftEndDateTime = new Date((new Date(shiftEndDate.setHours(shiftEndHour))).setMinutes(shiftEndMinutes));

				var totalShiftTimeInMs = shiftEndDateTime - shiftStartDateTime;

				var task1StartTime = shiftStartDateTime.getTime();
				var task1EndTime = -1, task2StartTime = -1, task2EndTime = -1;
				
				if(empTasks.length < 2) { //only one task
					task1EndTime = shiftEndDateTime.getTime();
				}
				else {
					task1EndTime = task1StartTime + (totalShiftTimeInMs * 0.25);
					task2StartTime = task1EndTime;
					task2EndTime = shiftEndDateTime.getTime();
				}
				
				var nightShift = shiftType.nightShift;

				for (var taskIndex = 0; taskIndex < empTasks.length; taskIndex++) {

					var startTime = ((taskIndex + 1) % 2 != 0) ? task1StartTime : task2StartTime;
					var endTime = ((taskIndex + 1) % 2 != 0) ? task1EndTime : task2EndTime;

					empShiftData.times.push({
						"id" : (taskIndex + 1),
						"class" : ("border" + "_" + (nightShift ? "black" : "red")),
						"label" : empTasks[taskIndex].description,
						"starting_time" : startTime,
						"ending_time" : endTime
					});

					dateObjects.push(new Date(startTime));
					dateObjects.push(new Date(endTime));
				}

				vm.timelineData.push(empShiftData);

			}

			vm.maxShiftTime = new Date(Math.max.apply(null, dateObjects)).getTime() + 1 * 60 * 60 * 1000;
			vm.minShiftTime = new Date(Math.min.apply(null, dateObjects)).getTime() - 1 * 60 * 60 * 1000;

		};
	}
})();