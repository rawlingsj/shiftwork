'use strict';

describe('Controller Tests', function() {

    describe('Shift Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockShift, MockShiftType, MockShiftDate;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockShift = jasmine.createSpy('MockShift');
            MockShiftType = jasmine.createSpy('MockShiftType');
            MockShiftDate = jasmine.createSpy('MockShiftDate');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Shift': MockShift,
                'ShiftType': MockShiftType,
                'ShiftDate': MockShiftDate
            };
            createController = function() {
                $injector.get('$controller')("ShiftDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'shiftworkApp:shiftUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
