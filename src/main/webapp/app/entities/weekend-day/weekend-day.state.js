(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('weekend-day', {
            parent: 'entity',
            url: '/weekend-day',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'shiftworkApp.weekendDay.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/weekend-day/weekend-days.html',
                    controller: 'WeekendDayController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('weekendDay');
                    $translatePartialLoader.addPart('dayOfWeek');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('weekend-day-detail', {
            parent: 'entity',
            url: '/weekend-day/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'shiftworkApp.weekendDay.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/weekend-day/weekend-day-detail.html',
                    controller: 'WeekendDayDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('weekendDay');
                    $translatePartialLoader.addPart('dayOfWeek');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'WeekendDay', function($stateParams, WeekendDay) {
                    return WeekendDay.get({id : $stateParams.id});
                }]
            }
        })
        .state('weekend-day.new', {
            parent: 'weekend-day',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/weekend-day/weekend-day-dialog.html',
                    controller: 'WeekendDayDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                dayOfWeek: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('weekend-day', null, { reload: true });
                }, function() {
                    $state.go('weekend-day');
                });
            }]
        })
        .state('weekend-day.edit', {
            parent: 'weekend-day',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/weekend-day/weekend-day-dialog.html',
                    controller: 'WeekendDayDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['WeekendDay', function(WeekendDay) {
                            return WeekendDay.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('weekend-day', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('weekend-day.delete', {
            parent: 'weekend-day',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/weekend-day/weekend-day-delete-dialog.html',
                    controller: 'WeekendDayDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['WeekendDay', function(WeekendDay) {
                            return WeekendDay.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('weekend-day', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
