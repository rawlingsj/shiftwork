(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('contract', {
            parent: 'entity',
            url: '/contract',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'shiftworkApp.contract.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/contract/contracts.html',
                    controller: 'ContractController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('contract');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('contract-detail', {
            parent: 'entity',
            url: '/contract/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'shiftworkApp.contract.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/contract/contract-detail.html',
                    controller: 'ContractDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('contract');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Contract', function($stateParams, Contract) {
                    return Contract.get({id : $stateParams.id});
                }]
            }
        })
        .state('contract.new', {
            parent: 'contract',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/contract/contract-dialog.html',
                    controller: 'ContractDialogController',
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
                    $state.go('contract', null, { reload: true });
                }, function() {
                    $state.go('contract');
                });
            }]
        })
        .state('contract.edit', {
            parent: 'contract',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/contract/contract-dialog.html',
                    controller: 'ContractDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Contract', function(Contract) {
                            return Contract.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('contract', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('contract.delete', {
            parent: 'contract',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/contract/contract-delete-dialog.html',
                    controller: 'ContractDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Contract', function(Contract) {
                            return Contract.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('contract', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
