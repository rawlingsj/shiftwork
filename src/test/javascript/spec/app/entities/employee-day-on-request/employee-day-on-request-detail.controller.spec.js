'use strict';

describe('Controller Tests', function() {

    describe('EmployeeDayOnRequest Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockEmployeeDayOnRequest, MockShiftDate, MockEmployee;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockEmployeeDayOnRequest = jasmine.createSpy('MockEmployeeDayOnRequest');
            MockShiftDate = jasmine.createSpy('MockShiftDate');
            MockEmployee = jasmine.createSpy('MockEmployee');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'EmployeeDayOnRequest': MockEmployeeDayOnRequest,
                'ShiftDate': MockShiftDate,
                'Employee': MockEmployee
            };
            createController = function() {
                $injector.get('$controller')("EmployeeDayOnRequestDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'shiftworkApp:employeeDayOnRequestUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
