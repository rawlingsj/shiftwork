'use strict';

describe('Controller Tests', function() {

    describe('Task Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockTask, MockShiftAssignment;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockTask = jasmine.createSpy('MockTask');
            MockShiftAssignment = jasmine.createSpy('MockShiftAssignment');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Task': MockTask,
                'ShiftAssignment': MockShiftAssignment
            };
            createController = function() {
                $injector.get('$controller')("TaskDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'shiftworkApp:taskUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
