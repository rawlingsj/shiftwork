'use strict';

describe('Controller Tests', function() {

    describe('SkillProficiency Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockSkillProficiency, MockSkill, MockEmployee;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockSkillProficiency = jasmine.createSpy('MockSkillProficiency');
            MockSkill = jasmine.createSpy('MockSkill');
            MockEmployee = jasmine.createSpy('MockEmployee');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'SkillProficiency': MockSkillProficiency,
                'Skill': MockSkill,
                'Employee': MockEmployee
            };
            createController = function() {
                $injector.get('$controller')("SkillProficiencyDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'shiftworkApp:skillProficiencyUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
