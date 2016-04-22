(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('contract-line', {
            parent: 'entity',
            url: '/contract-line',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'shiftworkApp.contractLine.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/contract-line/contract-lines.html',
                    controller: 'ContractLineController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('contractLine');
                    $translatePartialLoader.addPart('contractLineType');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('contract-line-detail', {
            parent: 'entity',
            url: '/contract-line/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'shiftworkApp.contractLine.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/contract-line/contract-line-detail.html',
                    controller: 'ContractLineDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('contractLine');
                    $translatePartialLoader.addPart('contractLineType');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ContractLine', function($stateParams, ContractLine) {
                    return ContractLine.get({id : $stateParams.id});
                }]
            }
        })
        .state('contract-line.new', {
            parent: 'contract-line',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/contract-line/contract-line-dialog.html',
                    controller: 'ContractLineDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                contractLineType: null,
                                enabled: null,
                                weight: null,
                                minimumEnabled: null,
                                minimumValue: null,
                                minimumWeight: null,
                                maximumEnabled: null,
                                maximumValue: null,
                                maximumWeight: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('contract-line', null, { reload: true });
                }, function() {
                    $state.go('contract-line');
                });
            }]
        })
        .state('contract-line.edit', {
            parent: 'contract-line',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/contract-line/contract-line-dialog.html',
                    controller: 'ContractLineDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ContractLine', function(ContractLine) {
                            return ContractLine.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('contract-line', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('contract-line.delete', {
            parent: 'contract-line',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/contract-line/contract-line-delete-dialog.html',
                    controller: 'ContractLineDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ContractLine', function(ContractLine) {
                            return ContractLine.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('contract-line', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
