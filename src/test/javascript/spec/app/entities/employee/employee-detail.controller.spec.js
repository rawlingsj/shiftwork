'use strict';

describe('Controller Tests', function() {

    describe('Employee Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockEmployee, MockContract, MockEmployeeDayOffRequest, MockEmployeeDayOnRequest, MockEmployeeShiftOffRequest, MockEmployeeShiftOnRequest, MockEmployeeLeaveAbsence;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockEmployee = jasmine.createSpy('MockEmployee');
            MockContract = jasmine.createSpy('MockContract');
            MockEmployeeDayOffRequest = jasmine.createSpy('MockEmployeeDayOffRequest');
            MockEmployeeDayOnRequest = jasmine.createSpy('MockEmployeeDayOnRequest');
            MockEmployeeShiftOffRequest = jasmine.createSpy('MockEmployeeShiftOffRequest');
            MockEmployeeShiftOnRequest = jasmine.createSpy('MockEmployeeShiftOnRequest');
            MockEmployeeLeaveAbsence = jasmine.createSpy('MockEmployeeLeaveAbsence');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Employee': MockEmployee,
                'Contract': MockContract,
                'EmployeeDayOffRequest': MockEmployeeDayOffRequest,
                'EmployeeDayOnRequest': MockEmployeeDayOnRequest,
                'EmployeeShiftOffRequest': MockEmployeeShiftOffRequest,
                'EmployeeShiftOnRequest': MockEmployeeShiftOnRequest,
                'EmployeeLeaveAbsence': MockEmployeeLeaveAbsence
            };
            createController = function() {
                $injector.get('$controller')("EmployeeDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'shiftworkApp:employeeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
