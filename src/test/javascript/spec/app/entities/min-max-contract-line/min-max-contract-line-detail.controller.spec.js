'use strict';

describe('Controller Tests', function() {

    describe('MinMaxContractLine Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockMinMaxContractLine, MockContract;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockMinMaxContractLine = jasmine.createSpy('MockMinMaxContractLine');
            MockContract = jasmine.createSpy('MockContract');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'MinMaxContractLine': MockMinMaxContractLine,
                'Contract': MockContract
            };
            createController = function() {
                $injector.get('$controller')("MinMaxContractLineDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'shiftworkApp:minMaxContractLineUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
