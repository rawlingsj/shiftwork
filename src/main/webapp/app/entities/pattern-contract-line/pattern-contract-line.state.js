(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('pattern-contract-line', {
            parent: 'entity',
            url: '/pattern-contract-line',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'shiftworkApp.patternContractLine.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/pattern-contract-line/pattern-contract-lines.html',
                    controller: 'PatternContractLineController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('patternContractLine');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('pattern-contract-line-detail', {
            parent: 'entity',
            url: '/pattern-contract-line/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'shiftworkApp.patternContractLine.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/pattern-contract-line/pattern-contract-line-detail.html',
                    controller: 'PatternContractLineDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('patternContractLine');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'PatternContractLine', function($stateParams, PatternContractLine) {
                    return PatternContractLine.get({id : $stateParams.id});
                }]
            }
        })
        .state('pattern-contract-line.new', {
            parent: 'pattern-contract-line',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/pattern-contract-line/pattern-contract-line-dialog.html',
                    controller: 'PatternContractLineDialogController',
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
                    $state.go('pattern-contract-line', null, { reload: true });
                }, function() {
                    $state.go('pattern-contract-line');
                });
            }]
        })
        .state('pattern-contract-line.edit', {
            parent: 'pattern-contract-line',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/pattern-contract-line/pattern-contract-line-dialog.html',
                    controller: 'PatternContractLineDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PatternContractLine', function(PatternContractLine) {
                            return PatternContractLine.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('pattern-contract-line', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('pattern-contract-line.delete', {
            parent: 'pattern-contract-line',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/pattern-contract-line/pattern-contract-line-delete-dialog.html',
                    controller: 'PatternContractLineDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['PatternContractLine', function(PatternContractLine) {
                            return PatternContractLine.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('pattern-contract-line', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
