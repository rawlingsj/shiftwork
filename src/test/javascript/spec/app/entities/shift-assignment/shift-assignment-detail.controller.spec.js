'use strict';

describe('Controller Tests', function() {

    describe('ShiftAssignment Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockShiftAssignment, MockShift, MockEmployee;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockShiftAssignment = jasmine.createSpy('MockShiftAssignment');
            MockShift = jasmine.createSpy('MockShift');
            MockEmployee = jasmine.createSpy('MockEmployee');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'ShiftAssignment': MockShiftAssignment,
                'Shift': MockShift,
                'Employee': MockEmployee
            };
            createController = function() {
                $injector.get('$controller')("ShiftAssignmentDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'shiftworkApp:shiftAssignmentUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
