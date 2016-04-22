(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('shift-type', {
            parent: 'entity',
            url: '/shift-type',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'shiftworkApp.shiftType.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/shift-type/shift-types.html',
                    controller: 'ShiftTypeController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('shiftType');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('shift-type-detail', {
            parent: 'entity',
            url: '/shift-type/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'shiftworkApp.shiftType.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/shift-type/shift-type-detail.html',
                    controller: 'ShiftTypeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('shiftType');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ShiftType', function($stateParams, ShiftType) {
                    return ShiftType.get({id : $stateParams.id});
                }]
            }
        })
        .state('shift-type.new', {
            parent: 'shift-type',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/shift-type/shift-type-dialog.html',
                    controller: 'ShiftTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                code: null,
                                description: null,
                                nightShift: null,
                                startTime: null,
                                endTime: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('shift-type', null, { reload: true });
                }, function() {
                    $state.go('shift-type');
                });
            }]
        })
        .state('shift-type.edit', {
            parent: 'shift-type',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/shift-type/shift-type-dialog.html',
                    controller: 'ShiftTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ShiftType', function(ShiftType) {
                            return ShiftType.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('shift-type', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('shift-type.delete', {
            parent: 'shift-type',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/shift-type/shift-type-delete-dialog.html',
                    controller: 'ShiftTypeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ShiftType', function(ShiftType) {
                            return ShiftType.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('shift-type', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
