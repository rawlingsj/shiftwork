'use strict';

describe('Controller Tests', function() {

    describe('ShiftDate Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockShiftDate, MockShift;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockShiftDate = jasmine.createSpy('MockShiftDate');
            MockShift = jasmine.createSpy('MockShift');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'ShiftDate': MockShiftDate,
                'Shift': MockShift
            };
            createController = function() {
                $injector.get('$controller')("ShiftDateDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'shiftworkApp:shiftDateUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
