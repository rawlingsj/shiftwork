(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('shift', {
            parent: 'entity',
            url: '/shift',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'shiftworkApp.shift.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/shift/shifts.html',
                    controller: 'ShiftController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('shift');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('shift-detail', {
            parent: 'entity',
            url: '/shift/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'shiftworkApp.shift.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/shift/shift-detail.html',
                    controller: 'ShiftDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('shift');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Shift', function($stateParams, Shift) {
                    return Shift.get({id : $stateParams.id});
                }]
            }
        })
        .state('shift.new', {
            parent: 'shift',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/shift/shift-dialog.html',
                    controller: 'ShiftDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                index: null,
                                staffRequired: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('shift', null, { reload: true });
                }, function() {
                    $state.go('shift');
                });
            }]
        })
        .state('shift.edit', {
            parent: 'shift',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/shift/shift-dialog.html',
                    controller: 'ShiftDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Shift', function(Shift) {
                            return Shift.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('shift', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('shift.delete', {
            parent: 'shift',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/shift/shift-delete-dialog.html',
                    controller: 'ShiftDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Shift', function(Shift) {
                            return Shift.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('shift', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
