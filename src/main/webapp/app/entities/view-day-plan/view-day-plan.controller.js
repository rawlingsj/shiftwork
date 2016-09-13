(function () {
	'use strict';

	angular
	.module('shiftworkApp')
	.controller('ViewDayPlanController', ViewDayPlanController);

	ViewDayPlanController.$inject = ['$scope', '$state', 'ViewDayPlan', '$translate'];

	function ViewDayPlanController($scope, $state, ViewDayPlan, $translate) {

		var vm = this;

		vm.shiftAssignments = new Array();
		
		vm.loadShiftAssignments = function (shiftDay) {
			ViewDayPlan.query({
				fromShiftDate : shiftDay,
				toShiftDate : shiftDay
			}, function (result) {
				vm.shiftAssignments = result;
				vm.formatShiftDataForTimeline();
				vm.generateTimeLine();
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
				});

			var svg = chartElem
				.append("div")
				.classed("svg-container", true)
				.append("svg")
				.attr("width",parentWidth)
				.attr("preserveAspectRatio", "xMinYMin meet")
				.attr("viewBox", "0 0 " + (parentWidth) + " " + (450))
				.datum(vm.timelineData)
				.call(chart);
				
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
				empShiftData.times = new Array();

				for (var shiftIndex = 0; shiftIndex < vm.shiftAssignments[index].shift.length; shiftIndex++) {

					var empTasks = vm.shiftAssignments[index].shift[shiftIndex].shiftType.tasks;
					var oddShift = ((shiftIndex + 1) % 2 == 0);

					for (var taskIndex = 0; taskIndex < empTasks.length; taskIndex++) {

						empShiftData.times.push({
							"id" : ("border" + "_" + (oddShift ? "red" : "black")),
							"starting_time" : empTasks[taskIndex].startTime,
							"ending_time" : empTasks[taskIndex].endTime
						});

						dateObjects.push(new Date(empTasks[taskIndex].startTime));
						dateObjects.push(new Date(empTasks[taskIndex].endTime));
					}

				}

				vm.timelineData.push(empShiftData);

			}

			vm.maxShiftTime = new Date(Math.max.apply(null, dateObjects)).getTime();
			vm.minShiftTime = new Date(Math.min.apply(null, dateObjects)).getTime();

		};
	}
})();