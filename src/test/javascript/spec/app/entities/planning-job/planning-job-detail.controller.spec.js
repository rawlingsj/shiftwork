'use strict';

describe('Controller Tests', function() {

    describe('PlanningJob Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPlanningJob, MockStaffRosterParametrization;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPlanningJob = jasmine.createSpy('MockPlanningJob');
            MockStaffRosterParametrization = jasmine.createSpy('MockStaffRosterParametrization');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'PlanningJob': MockPlanningJob,
                'StaffRosterParametrization': MockStaffRosterParametrization
            };
            createController = function() {
                $injector.get('$controller')("PlanningJobDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'shiftworkApp:planningJobUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
