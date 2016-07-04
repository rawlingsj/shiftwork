(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('weekend-definition', {
            parent: 'entity',
            url: '/weekend-definition',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'shiftworkApp.weekendDefinition.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/weekend-definition/weekend-definitions.html',
                    controller: 'WeekendDefinitionController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('weekendDefinition');
                    $translatePartialLoader.addPart('dayOfWeek');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('weekend-definition-detail', {
            parent: 'entity',
            url: '/weekend-definition/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'shiftworkApp.weekendDefinition.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/weekend-definition/weekend-definition-detail.html',
                    controller: 'WeekendDefinitionDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('weekendDefinition');
                    $translatePartialLoader.addPart('dayOfWeek');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'WeekendDefinition', function($stateParams, WeekendDefinition) {
                    return WeekendDefinition.get({id : $stateParams.id});
                }]
            }
        })
        .state('weekend-definition.new', {
            parent: 'weekend-definition',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/weekend-definition/weekend-definition-dialog.html',
                    controller: 'WeekendDefinitionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                description: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('weekend-definition', null, { reload: true });
                }, function() {
                    $state.go('weekend-definition');
                });
            }]
        })
        .state('weekend-definition.edit', {
            parent: 'weekend-definition',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/weekend-definition/weekend-definition-dialog.html',
                    controller: 'WeekendDefinitionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['WeekendDefinition', function(WeekendDefinition) {
                            return WeekendDefinition.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('weekend-definition', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('weekend-definition.delete', {
            parent: 'weekend-definition',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/weekend-definition/weekend-definition-delete-dialog.html',
                    controller: 'WeekendDefinitionDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['WeekendDefinition', function(WeekendDefinition) {
                            return WeekendDefinition.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('weekend-definition', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
