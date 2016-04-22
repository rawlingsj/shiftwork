'use strict';

describe('Controller Tests', function() {

    describe('EmployeeShiftOnRequest Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockEmployeeShiftOnRequest, MockShift, MockEmployee;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockEmployeeShiftOnRequest = jasmine.createSpy('MockEmployeeShiftOnRequest');
            MockShift = jasmine.createSpy('MockShift');
            MockEmployee = jasmine.createSpy('MockEmployee');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'EmployeeShiftOnRequest': MockEmployeeShiftOnRequest,
                'Shift': MockShift,
                'Employee': MockEmployee
            };
            createController = function() {
                $injector.get('$controller')("EmployeeShiftOnRequestDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'shiftworkApp:employeeShiftOnRequestUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
