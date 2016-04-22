'use strict';

describe('Controller Tests', function() {

    describe('WeekendDefinition Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockWeekendDefinition, MockWeekendDay;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockWeekendDefinition = jasmine.createSpy('MockWeekendDefinition');
            MockWeekendDay = jasmine.createSpy('MockWeekendDay');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'WeekendDefinition': MockWeekendDefinition,
                'WeekendDay': MockWeekendDay
            };
            createController = function() {
                $injector.get('$controller')("WeekendDefinitionDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'shiftworkApp:weekendDefinitionUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
