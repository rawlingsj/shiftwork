'use strict';

describe('Controller Tests', function() {

    describe('TaskSkillRequirement Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockTaskSkillRequirement, MockTask, MockSkill;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockTaskSkillRequirement = jasmine.createSpy('MockTaskSkillRequirement');
            MockTask = jasmine.createSpy('MockTask');
            MockSkill = jasmine.createSpy('MockSkill');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'TaskSkillRequirement': MockTaskSkillRequirement,
                'Task': MockTask,
                'Skill': MockSkill
            };
            createController = function() {
                $injector.get('$controller')("TaskSkillRequirementDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'shiftworkApp:taskSkillRequirementUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
