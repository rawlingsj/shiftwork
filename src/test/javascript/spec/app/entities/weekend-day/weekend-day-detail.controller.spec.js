'use strict';

describe('Controller Tests', function() {

    describe('WeekendDay Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockWeekendDay, MockWeekendDefinition;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockWeekendDay = jasmine.createSpy('MockWeekendDay');
            MockWeekendDefinition = jasmine.createSpy('MockWeekendDefinition');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'WeekendDay': MockWeekendDay,
                'WeekendDefinition': MockWeekendDefinition
            };
            createController = function() {
                $injector.get('$controller')("WeekendDayDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'shiftworkApp:weekendDayUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
