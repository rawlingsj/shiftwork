(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('boolean-contract-line', {
            parent: 'entity',
            url: '/boolean-contract-line',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'shiftworkApp.booleanContractLine.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/boolean-contract-line/boolean-contract-lines.html',
                    controller: 'BooleanContractLineController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('booleanContractLine');
                    $translatePartialLoader.addPart('contractLineType');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('boolean-contract-line-detail', {
            parent: 'entity',
            url: '/boolean-contract-line/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'shiftworkApp.booleanContractLine.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/boolean-contract-line/boolean-contract-line-detail.html',
                    controller: 'BooleanContractLineDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('booleanContractLine');
                    $translatePartialLoader.addPart('contractLineType');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'BooleanContractLine', function($stateParams, BooleanContractLine) {
                    return BooleanContractLine.get({id : $stateParams.id});
                }]
            }
        })
        .state('boolean-contract-line.new', {
            parent: 'boolean-contract-line',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/boolean-contract-line/boolean-contract-line-dialog.html',
                    controller: 'BooleanContractLineDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                type: 'boolean',
                                contractLineType: null,
                                enabled: null,
                                weight: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('boolean-contract-line', null, { reload: true });
                }, function() {
                    $state.go('boolean-contract-line');
                });
            }]
        })
        .state('boolean-contract-line.edit', {
            parent: 'boolean-contract-line',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/boolean-contract-line/boolean-contract-line-dialog.html',
                    controller: 'BooleanContractLineDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['BooleanContractLine', function(BooleanContractLine) {
                            return BooleanContractLine.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('boolean-contract-line', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('boolean-contract-line.delete', {
            parent: 'boolean-contract-line',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/boolean-contract-line/boolean-contract-line-delete-dialog.html',
                    controller: 'BooleanContractLineDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['BooleanContractLine', function(BooleanContractLine) {
                            return BooleanContractLine.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('boolean-contract-line', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
