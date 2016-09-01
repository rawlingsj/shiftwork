'use strict';

describe('Controller Tests', function() {

    describe('EmployeeAbsentReason Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockEmployeeAbsentReason;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockEmployeeAbsentReason = jasmine.createSpy('MockEmployeeAbsentReason');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'EmployeeAbsentReason': MockEmployeeAbsentReason
            };
            createController = function() {
                $injector.get('$controller')("EmployeeAbsentReasonDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'shiftworkApp:employeeAbsentReasonUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
