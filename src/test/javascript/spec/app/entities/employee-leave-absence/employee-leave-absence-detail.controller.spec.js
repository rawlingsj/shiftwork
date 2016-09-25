'use strict';

describe('Controller Tests', function() {

    describe('EmployeeLeaveAbsence Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockEmployeeLeaveAbsence, MockEmployeeAbsentReason, MockEmployee;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockEmployeeLeaveAbsence = jasmine.createSpy('MockEmployeeLeaveAbsence');
            MockEmployeeAbsentReason = jasmine.createSpy('MockEmployeeAbsentReason');
            MockEmployee = jasmine.createSpy('MockEmployee');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'EmployeeLeaveAbsence': MockEmployeeLeaveAbsence,
                'EmployeeAbsentReason': MockEmployeeAbsentReason,
                'Employee': MockEmployee
            };
            createController = function() {
                $injector.get('$controller')("EmployeeLeaveAbsenceDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'shiftworkApp:employeeLeaveAbsenceUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
