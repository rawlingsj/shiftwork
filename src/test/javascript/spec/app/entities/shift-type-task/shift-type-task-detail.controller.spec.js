'use strict';

describe('Controller Tests', function() {

    describe('ShiftTypeTask Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockShiftTypeTask, MockTask, MockShiftType;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockShiftTypeTask = jasmine.createSpy('MockShiftTypeTask');
            MockTask = jasmine.createSpy('MockTask');
            MockShiftType = jasmine.createSpy('MockShiftType');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'ShiftTypeTask': MockShiftTypeTask,
                'Task': MockTask,
                'ShiftType': MockShiftType
            };
            createController = function() {
                $injector.get('$controller')("ShiftTypeTaskDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'shiftworkApp:shiftTypeTaskUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
