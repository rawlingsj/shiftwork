'use strict';

describe('Controller Tests', function() {

    describe('EmployeeDayOffRequest Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockEmployeeDayOffRequest, MockShiftDate, MockEmployee;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockEmployeeDayOffRequest = jasmine.createSpy('MockEmployeeDayOffRequest');
            MockShiftDate = jasmine.createSpy('MockShiftDate');
            MockEmployee = jasmine.createSpy('MockEmployee');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'EmployeeDayOffRequest': MockEmployeeDayOffRequest,
                'ShiftDate': MockShiftDate,
                'Employee': MockEmployee
            };
            createController = function() {
                $injector.get('$controller')("EmployeeDayOffRequestDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'shiftworkApp:employeeDayOffRequestUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
