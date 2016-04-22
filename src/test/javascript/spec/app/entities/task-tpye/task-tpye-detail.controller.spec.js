'use strict';

describe('Controller Tests', function() {

    describe('TaskTpye Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockTaskTpye;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockTaskTpye = jasmine.createSpy('MockTaskTpye');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'TaskTpye': MockTaskTpye
            };
            createController = function() {
                $injector.get('$controller')("TaskTpyeDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'shiftworkApp:taskTpyeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
