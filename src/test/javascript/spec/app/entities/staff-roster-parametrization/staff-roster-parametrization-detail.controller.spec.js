'use strict';

describe('Controller Tests', function() {

    describe('StaffRosterParametrization Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockStaffRosterParametrization, MockShiftDate;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockStaffRosterParametrization = jasmine.createSpy('MockStaffRosterParametrization');
            MockShiftDate = jasmine.createSpy('MockShiftDate');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'StaffRosterParametrization': MockStaffRosterParametrization,
                'ShiftDate': MockShiftDate
            };
            createController = function() {
                $injector.get('$controller')("StaffRosterParametrizationDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'shiftworkApp:staffRosterParametrizationUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
