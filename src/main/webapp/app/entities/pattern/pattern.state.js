(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('pattern', {
            parent: 'entity',
            url: '/pattern',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'shiftworkApp.pattern.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/pattern/patterns.html',
                    controller: 'PatternController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('pattern');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('pattern-detail', {
            parent: 'entity',
            url: '/pattern/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'shiftworkApp.pattern.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/pattern/pattern-detail.html',
                    controller: 'PatternDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('pattern');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Pattern', function($stateParams, Pattern) {
                    return Pattern.get({id : $stateParams.id});
                }]
            }
        })
        .state('pattern.new', {
            parent: 'pattern',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/pattern/pattern-dialog.html',
                    controller: 'PatternDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                code: null,
                                weight: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('pattern', null, { reload: true });
                }, function() {
                    $state.go('pattern');
                });
            }]
        })
        .state('pattern.edit', {
            parent: 'pattern',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/pattern/pattern-dialog.html',
                    controller: 'PatternDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Pattern', function(Pattern) {
                            return Pattern.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('pattern', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('pattern.delete', {
            parent: 'pattern',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/pattern/pattern-delete-dialog.html',
                    controller: 'PatternDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Pattern', function(Pattern) {
                            return Pattern.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('pattern', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
