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

                var DAYS_IN_PAST = 10
                var groups = []

                var assigment_data = [];
                var assigment_data_name = [];
                for(var j = 0; j < vm.shiftAssignments.length; j++) {
                    var sa = vm.shiftAssignments[j];
                    if(assigment_data_name.indexOf(sa.employee.name) == -1) {
                        var grdata = [];
                        for(var k = 0; k < vm.shiftAssignments.length; k++) {
                            var e = vm.shiftAssignments[k];
                            if(e.employee.name == sa.employee.name) {
                                grdata.push(e);
                            }
                        }
                        assigment_data.push({name: sa.employee.name, grdata : grdata});
                        assigment_data_name.push(sa.employee.name);
                    }
                };

                for (var i = 0; i < assigment_data_name.length; i++) {
                    groups.push({
                        id: (i+1)+'',
                        title: assigment_data_name[i]
                    })
                };

                var items = [];
                for (var i = 0; i < assigment_data.length; i++) {
                    var sa = assigment_data[i];
                    for(var m =0; m < sa.grdata.length; m++) {
                        var d = sa.grdata[m];
                        console.log(d);
                        // var startDate = faker.date.recent(DAYS_IN_PAST).valueOf() + (DAYS_IN_PAST * 0.3) * 86400 * 1000;
                        var startDate = new Date(d.shift.shiftDate.date);
                        var startValue = Math.floor(moment(startDate).valueOf() / 10000000) * 10000000;
                        var endValue = moment(startDate + 30 * 15 * 60 * 1000).valueOf();

                        items.push({
                            id: d.id + '',
                            group: (i + 1) + '',
                            title: sa.name,
                            start: startValue,
                            end: endValue,
                            canMove: startValue > new Date().getTime(),
                            canResize: startValue > new Date().getTime() ? (endValue > new Date().getTime() ? 'both' : 'left') : (endValue > new Date().getTime() ? 'right' : false),
                            className: (moment(startDate).day() === 6 || moment(startDate).day() === 0) ? 'item-weekend' : '',
                            itemProps: {
                                'data-tip': sa.name
                            }
                        })
                    }
                }

                items = items.sort(function(a, b) { return b - a; });
                var minTime = moment().add(-1, 'months').valueOf();
                var maxTime = moment().add(1, 'months').valueOf();
                var props = {
                    groups: groups,
                    items: items,
                    fixedHeader: 'fixed',
                    fullUpdate: true,
                    canMove: true, // defaults
                    canResize: 'right',
                    canSelect: true,
                    itemsSorted: true,
                    itemTouchSendsClick: false,
                    stackItems: true,
                    itemHeightRatio: 0.75,
                    defaultTimeStart: moment().startOf('day').toDate(),
                    defaultTimeEnd: moment().startOf('day').add(1, 'day').toDate(),
                    keys: {
                        groupIdKey: 'id',
                        groupTitleKey: 'title',
                        itemIdKey: 'id',
                        itemTitleKey: 'title',
                        itemDivTitleKey: 'title',
                        itemGroupKey: 'group',
                        itemTimeStartKey: 'start',
                        itemTimeEndKey: 'end'
                    },
                    onCanvasClick: function(event) {
                        console.log("Canvas clicked");
                    },
                    onItemClick: function(item) {
                        console.log("Clicked: " + item);
                    },
                    onItemSelect: function(item) {
                        console.log("Selected: " + item);
                    },
                    onItemContextMenu: function (item) {
                        console.log("Context Menu: " + item);
                    },
                    onItemMove: function (item, time) {
                        console.log("Moved", item, time);
                    },
                    onItemResize: function (item, time, edge) {
                        console.log("Resized", item, time, edge);
                    },
                    moveResizeValidator: function(action, item, time, resizeEdge) {
                        if (time < new Date().getTime()) {
                            var newTime = Math.ceil(new Date().getTime() / (15*60*1000)) * (15*60*1000);
                            return newTime;
                        }
                        return time
                    },
                    // this limits the timeline to -6 months ... +6 months
                    onTimeChange: function (visibleTimeStart, visibleTimeEnd, updateScrollCanvas) {
                        if (visibleTimeStart < minTime && visibleTimeEnd > maxTime) {
                            updateScrollCanvas(minTime, maxTime)
                        } else if (visibleTimeStart < minTime) {
                            updateScrollCanvas(minTime, minTime + (visibleTimeEnd - visibleTimeStart))
                        } else if (visibleTimeEnd > maxTime) {
                            updateScrollCanvas(maxTime - (visibleTimeEnd - visibleTimeStart), maxTime)
                        } else {
                            updateScrollCanvas(visibleTimeStart, visibleTimeEnd)
                        }
                    }
                }
                var filter = React.createElement("div", {}, "The filter");

                ReactDOM.render(React.createElement(ReactCalendarTimeline['default'], props, filter), document.getElementById('rct'));
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
