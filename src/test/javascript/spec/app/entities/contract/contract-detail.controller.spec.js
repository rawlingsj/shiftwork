'use strict';

describe('Controller Tests', function() {

    describe('Contract Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockContract, MockWeekendDefinition, MockContractLine;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockContract = jasmine.createSpy('MockContract');
            MockWeekendDefinition = jasmine.createSpy('MockWeekendDefinition');
            MockContractLine = jasmine.createSpy('MockContractLine');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Contract': MockContract,
                'WeekendDefinition': MockWeekendDefinition,
                'ContractLine': MockContractLine
            };
            createController = function() {
                $injector.get('$controller')("ContractDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'shiftworkApp:contractUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
