(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('staff-roster-parametrization', {
            parent: 'entity',
            url: '/staff-roster-parametrization',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'shiftworkApp.staffRosterParametrization.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/staff-roster-parametrization/staff-roster-parametrizations.html',
                    controller: 'StaffRosterParametrizationController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('staffRosterParametrization');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('staff-roster-parametrization-detail', {
            parent: 'entity',
            url: '/staff-roster-parametrization/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'shiftworkApp.staffRosterParametrization.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/staff-roster-parametrization/staff-roster-parametrization-detail.html',
                    controller: 'StaffRosterParametrizationDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('staffRosterParametrization');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'StaffRosterParametrization', function($stateParams, StaffRosterParametrization) {
                    return StaffRosterParametrization.get({id : $stateParams.id});
                }]
            }
        })
        .state('staff-roster-parametrization.new', {
            parent: 'staff-roster-parametrization',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/staff-roster-parametrization/staff-roster-parametrization-dialog.html',
                    controller: 'StaffRosterParametrizationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                description: null,
                                hardConstraintMatches: null,
                                softConstraintMatches: null,
                                timeMillisSpent: null,
                                lastRunTime: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('staff-roster-parametrization', null, { reload: true });
                }, function() {
                    $state.go('staff-roster-parametrization');
                });
            }]
        })
        .state('staff-roster-parametrization.edit', {
            parent: 'staff-roster-parametrization',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/staff-roster-parametrization/staff-roster-parametrization-dialog.html',
                    controller: 'StaffRosterParametrizationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['StaffRosterParametrization', function(StaffRosterParametrization) {
                            return StaffRosterParametrization.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('staff-roster-parametrization', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('staff-roster-parametrization.delete', {
            parent: 'staff-roster-parametrization',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/staff-roster-parametrization/staff-roster-parametrization-delete-dialog.html',
                    controller: 'StaffRosterParametrizationDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['StaffRosterParametrization', function(StaffRosterParametrization) {
                            return StaffRosterParametrization.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('staff-roster-parametrization', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
