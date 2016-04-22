'use strict';

describe('Controller Tests', function() {

    describe('PatternContractLine Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPatternContractLine, MockContract, MockPattern;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPatternContractLine = jasmine.createSpy('MockPatternContractLine');
            MockContract = jasmine.createSpy('MockContract');
            MockPattern = jasmine.createSpy('MockPattern');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'PatternContractLine': MockPatternContractLine,
                'Contract': MockContract,
                'Pattern': MockPattern
            };
            createController = function() {
                $injector.get('$controller')("PatternContractLineDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'shiftworkApp:patternContractLineUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
