(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('skill-proficiency', {
            parent: 'entity',
            url: '/skill-proficiency',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'shiftworkApp.skillProficiency.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/skill-proficiency/skill-proficiencies.html',
                    controller: 'SkillProficiencyController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('skillProficiency');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('skill-proficiency-detail', {
            parent: 'entity',
            url: '/skill-proficiency/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'shiftworkApp.skillProficiency.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/skill-proficiency/skill-proficiency-detail.html',
                    controller: 'SkillProficiencyDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('skillProficiency');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'SkillProficiency', function($stateParams, SkillProficiency) {
                    return SkillProficiency.get({id : $stateParams.id});
                }]
            }
        })
        .state('skill-proficiency.new', {
            parent: 'skill-proficiency',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/skill-proficiency/skill-proficiency-dialog.html',
                    controller: 'SkillProficiencyDialogController',
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
                    $state.go('skill-proficiency', null, { reload: true });
                }, function() {
                    $state.go('skill-proficiency');
                });
            }]
        })
        .state('skill-proficiency.edit', {
            parent: 'skill-proficiency',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/skill-proficiency/skill-proficiency-dialog.html',
                    controller: 'SkillProficiencyDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SkillProficiency', function(SkillProficiency) {
                            return SkillProficiency.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('skill-proficiency', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('skill-proficiency.delete', {
            parent: 'skill-proficiency',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/skill-proficiency/skill-proficiency-delete-dialog.html',
                    controller: 'SkillProficiencyDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['SkillProficiency', function(SkillProficiency) {
                            return SkillProficiency.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('skill-proficiency', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
