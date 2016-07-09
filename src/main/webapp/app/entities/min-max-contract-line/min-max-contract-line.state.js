(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('min-max-contract-line', {
            parent: 'entity',
            url: '/min-max-contract-line',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'shiftworkApp.minMaxContractLine.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/min-max-contract-line/min-max-contract-lines.html',
                    controller: 'MinMaxContractLineController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('minMaxContractLine');
                    $translatePartialLoader.addPart('contractLineType');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('min-max-contract-line-detail', {
            parent: 'entity',
            url: '/min-max-contract-line/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'shiftworkApp.minMaxContractLine.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/min-max-contract-line/min-max-contract-line-detail.html',
                    controller: 'MinMaxContractLineDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('minMaxContractLine');
                    $translatePartialLoader.addPart('contractLineType');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'MinMaxContractLine', function($stateParams, MinMaxContractLine) {
                    return MinMaxContractLine.get({id : $stateParams.id});
                }]
            }
        })
        .state('min-max-contract-line.new', {
            parent: 'min-max-contract-line',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/min-max-contract-line/min-max-contract-line-dialog.html',
                    controller: 'MinMaxContractLineDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                type: 'minmax',
                                contractLineType: null,
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
                    $state.go('min-max-contract-line', null, { reload: true });
                }, function() {
                    $state.go('min-max-contract-line');
                });
            }]
        })
        .state('min-max-contract-line.edit', {
            parent: 'min-max-contract-line',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/min-max-contract-line/min-max-contract-line-dialog.html',
                    controller: 'MinMaxContractLineDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MinMaxContractLine', function(MinMaxContractLine) {
                            return MinMaxContractLine.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('min-max-contract-line', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('min-max-contract-line.delete', {
            parent: 'min-max-contract-line',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/min-max-contract-line/min-max-contract-line-delete-dialog.html',
                    controller: 'MinMaxContractLineDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['MinMaxContractLine', function(MinMaxContractLine) {
                            return MinMaxContractLine.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('min-max-contract-line', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
