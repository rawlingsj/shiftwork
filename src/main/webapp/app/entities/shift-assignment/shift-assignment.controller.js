(function () {
    'use strict';

    angular
        .module('shiftworkApp')
        .controller('ShiftAssignmentController', ShiftAssignmentController)
        .directive('myTable', function(){
        return{
              restrict: 'E',
              scope:{
                  groups:'=',items:'='
            },
              link:function(scope, el, attrs){

                  scope.$watch('items', function(newValue, oldValue){
                  console.debug('----- directive scope -------');
                  console.debug(scope);

    var GROUP_COUNT = 30;
    var ITEM_COUNT = 10;
    var DAYS_IN_PAST = 30;

    var groups = scope.groups;
    var items = scope.items;
  

    var minTime = moment().add(-6, 'months').valueOf()
    var maxTime = moment().add(6, 'months').valueOf()


 var props = {
    groups: groups,
    items: items,
    fixedHeader: 'fixed',
    canMove: true, // defaults
    canResize: true,
    itemsSorted: true,
    itemTouchSendsClick: false,
    stackItems: true,
    itemHeightRatio: 0.75,
    dragSnap: moment.duration(1, 'days').asMilliseconds(),

    defaultTimeStart: moment().add(-7, 'day'),
    defaultTimeEnd: moment().add(7, 'day'),

    maxZoom: moment.duration(2, 'months').asMilliseconds(),
    minZoom: moment.duration(3, 'days').asMilliseconds()
  }



/*
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
    }*/



    var filter = React.createElement("div", {}, "The filter");
    ReactDOM.render(React.createElement(ReactCalendarTimeline['default'], props, filter), document.getElementById('main'));

                })
            }
        }
      });







    ShiftAssignmentController.$inject = ['$scope', '$state', 'ShiftAssignment', 'ShiftDate', '$http', '$q'];

    function ShiftAssignmentController($scope, $state, ShiftAssignment, ShiftDate, $http, $q) {



        var vm = this;

        vm.shiftAssignments = [];
        vm.shiftdates = ShiftDate.query();

        $scope.groups = [];
    

        var object = {};
        var id = 1;
        vm.loadAll = function () {
            ShiftAssignment.query(function (result) {

      
            vm.shiftAssignments = result;
            console.debug(vm.shiftAssignments);

            vm.shiftAssignments.forEach( function(s) { 
                id++;
                $scope.groups.push({ id: (id+1)+'', title: s.employee.name});
            });
 



            var GROUP_COUNT = vm.shiftAssignments.length;
           
            

            var TEM_COUNT = 1000;
            var DAYS_IN_PAST = 30;

            console.debug(GROUP_COUNT);

            });
        };

        vm.loadAll();



        console.debug('------Controller scope --------');
        console.debug($scope);

        console.debug('------api call ------');
        console.debug(vm.shiftAssignments);

       $scope.items = [
              {
                id: 1,
                group: 1,
                title: 'item 1',
                start: moment().add(1, 'day').hours(0).minutes(0).seconds(0),
                end: moment().add(4, 'day').hours(6).minutes(0).seconds(0)
              },
              {
                id: 2,
                group: 2,
                title: 'item 2',
                start: moment().add(-1, 'day').hours(0).minutes(0).seconds(0),
                end: moment().add(1, 'day').hours(0).minutes(0).seconds(0),
                 itemProps: {
                'data-tip': 'test'
               }
              },
              {
                id: 3,
                group: 2,
                title: 'item 3',
                start: moment().add(2, 'day').hours(0).minutes(0).seconds(0),
                end: moment().add(3, 'day').hours(0).minutes(0).seconds(0)
              }
      ];



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
