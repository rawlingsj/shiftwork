'use strict';

describe('Controller Tests', function() {

    describe('BooleanContractLine Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockBooleanContractLine, MockContract;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockBooleanContractLine = jasmine.createSpy('MockBooleanContractLine');
            MockContract = jasmine.createSpy('MockContract');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'BooleanContractLine': MockBooleanContractLine,
                'Contract': MockContract
            };
            createController = function() {
                $injector.get('$controller')("BooleanContractLineDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'shiftworkApp:booleanContractLineUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
