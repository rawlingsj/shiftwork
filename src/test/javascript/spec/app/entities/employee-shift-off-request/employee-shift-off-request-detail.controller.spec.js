'use strict';

describe('Controller Tests', function() {

    describe('EmployeeShiftOffRequest Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockEmployeeShiftOffRequest, MockShift, MockEmployee;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockEmployeeShiftOffRequest = jasmine.createSpy('MockEmployeeShiftOffRequest');
            MockShift = jasmine.createSpy('MockShift');
            MockEmployee = jasmine.createSpy('MockEmployee');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'EmployeeShiftOffRequest': MockEmployeeShiftOffRequest,
                'Shift': MockShift,
                'Employee': MockEmployee
            };
            createController = function() {
                $injector.get('$controller')("EmployeeShiftOffRequestDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'shiftworkApp:employeeShiftOffRequestUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
