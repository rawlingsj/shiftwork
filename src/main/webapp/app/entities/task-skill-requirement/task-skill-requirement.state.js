(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('task-skill-requirement', {
            parent: 'entity',
            url: '/task-skill-requirement',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'shiftworkApp.taskSkillRequirement.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/task-skill-requirement/task-skill-requirements.html',
                    controller: 'TaskSkillRequirementController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('taskSkillRequirement');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('task-skill-requirement-detail', {
            parent: 'entity',
            url: '/task-skill-requirement/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'shiftworkApp.taskSkillRequirement.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/task-skill-requirement/task-skill-requirement-detail.html',
                    controller: 'TaskSkillRequirementDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('taskSkillRequirement');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'TaskSkillRequirement', function($stateParams, TaskSkillRequirement) {
                    return TaskSkillRequirement.get({id : $stateParams.id});
                }]
            }
        })
        .state('task-skill-requirement.new', {
            parent: 'task-skill-requirement',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/task-skill-requirement/task-skill-requirement-dialog.html',
                    controller: 'TaskSkillRequirementDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('task-skill-requirement', null, { reload: true });
                }, function() {
                    $state.go('task-skill-requirement');
                });
            }]
        })
        .state('task-skill-requirement.edit', {
            parent: 'task-skill-requirement',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/task-skill-requirement/task-skill-requirement-dialog.html',
                    controller: 'TaskSkillRequirementDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TaskSkillRequirement', function(TaskSkillRequirement) {
                            return TaskSkillRequirement.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('task-skill-requirement', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('task-skill-requirement.delete', {
            parent: 'task-skill-requirement',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/task-skill-requirement/task-skill-requirement-delete-dialog.html',
                    controller: 'TaskSkillRequirementDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['TaskSkillRequirement', function(TaskSkillRequirement) {
                            return TaskSkillRequirement.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('task-skill-requirement', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
