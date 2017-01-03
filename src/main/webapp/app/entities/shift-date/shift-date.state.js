(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('shift-date', {
            parent: 'entity',
            url: '/shift-date',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'shiftworkApp.shiftDate.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/shift-date/shift-dates.html',
                    controller: 'ShiftDateController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('shiftDate');
                    $translatePartialLoader.addPart('dayOfWeek');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('shift-date-detail', {
            parent: 'entity',
            url: '/shift-date/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'shiftworkApp.shiftDate.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/shift-date/shift-date-detail.html',
                    controller: 'ShiftDateDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('shiftDate');
                    $translatePartialLoader.addPart('dayOfWeek');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ShiftDate', function($stateParams, ShiftDate) {
                    return ShiftDate.get({id : $stateParams.id});
                }]
            }
        })
        .state('shift-date.new', {
            parent: 'shift-date',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/shift-date/shift-date-dialog.html',
                    controller: 'ShiftDateDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                dayIndex: null,
                                date: null,
                                dayOfWeek: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('shift-date', null, { reload: true });
                }, function() {
                    $state.go('shift-date');
                });
            }]
        })
        .state('shift-date.newMultiple', {
            parent: 'shift-date',
            url: '/newMultiple',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/shift-date/shift-date-dialog-multiple.html',
                    controller: 'ShiftDateDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                dayIndex: null,
                                date: null,
                                dayOfWeek: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('shift-date', null, { reload: true });
                }, function() {
                    $state.go('shift-date');
                });
            }]
        })
        .state('shift-date.edit', {
            parent: 'shift-date',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/shift-date/shift-date-dialog.html',
                    controller: 'ShiftDateDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ShiftDate', function(ShiftDate) {
                            return ShiftDate.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('shift-date', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('shift-date.delete', {
            parent: 'shift-date',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/shift-date/shift-date-delete-dialog.html',
                    controller: 'ShiftDateDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ShiftDate', function(ShiftDate) {
                            return ShiftDate.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('shift-date', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
