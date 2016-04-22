'use strict';

describe('Controller Tests', function() {

    describe('Employee Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockEmployee, MockContract, MockWeekendDefinition, MockSkillProficiency, MockEmployeeDayOffRequest, MockEmployeeDayOnRequest, MockEmployeeShiftOffRequest, MockEmployeeShiftOnRequest;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockEmployee = jasmine.createSpy('MockEmployee');
            MockContract = jasmine.createSpy('MockContract');
            MockWeekendDefinition = jasmine.createSpy('MockWeekendDefinition');
            MockSkillProficiency = jasmine.createSpy('MockSkillProficiency');
            MockEmployeeDayOffRequest = jasmine.createSpy('MockEmployeeDayOffRequest');
            MockEmployeeDayOnRequest = jasmine.createSpy('MockEmployeeDayOnRequest');
            MockEmployeeShiftOffRequest = jasmine.createSpy('MockEmployeeShiftOffRequest');
            MockEmployeeShiftOnRequest = jasmine.createSpy('MockEmployeeShiftOnRequest');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Employee': MockEmployee,
                'Contract': MockContract,
                'WeekendDefinition': MockWeekendDefinition,
                'SkillProficiency': MockSkillProficiency,
                'EmployeeDayOffRequest': MockEmployeeDayOffRequest,
                'EmployeeDayOnRequest': MockEmployeeDayOnRequest,
                'EmployeeShiftOffRequest': MockEmployeeShiftOffRequest,
                'EmployeeShiftOnRequest': MockEmployeeShiftOnRequest
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
