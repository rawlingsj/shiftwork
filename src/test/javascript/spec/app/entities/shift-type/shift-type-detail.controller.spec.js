'use strict';

describe('Controller Tests', function() {

    describe('ShiftType Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockShiftType, MockShiftTypeTask;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockShiftType = jasmine.createSpy('MockShiftType');
            MockShiftTypeTask = jasmine.createSpy('MockShiftTypeTask');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'ShiftType': MockShiftType,
                'ShiftTypeTask': MockShiftTypeTask
            };
            createController = function() {
                $injector.get('$controller')("ShiftTypeDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'shiftworkApp:shiftTypeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
