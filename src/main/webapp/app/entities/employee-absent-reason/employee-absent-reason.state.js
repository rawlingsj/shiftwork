(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('employee-absent-reason', {
            parent: 'entity',
            url: '/employee-absent-reason',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'shiftworkApp.employeeAbsentReason.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/employee-absent-reason/employee-absent-reasons.html',
                    controller: 'EmployeeAbsentReasonController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('employeeAbsentReason');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('employee-absent-reason-detail', {
            parent: 'entity',
            url: '/employee-absent-reason/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'shiftworkApp.employeeAbsentReason.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/employee-absent-reason/employee-absent-reason-detail.html',
                    controller: 'EmployeeAbsentReasonDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('employeeAbsentReason');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'EmployeeAbsentReason', function($stateParams, EmployeeAbsentReason) {
                    return EmployeeAbsentReason.get({id : $stateParams.id});
                }]
            }
        })
        .state('employee-absent-reason.new', {
            parent: 'employee-absent-reason',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/employee-absent-reason/employee-absent-reason-dialog.html',
                    controller: 'EmployeeAbsentReasonDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                code: null,
                                name: null,
                                description: null,
                                defaultDuration: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('employee-absent-reason', null, { reload: true });
                }, function() {
                    $state.go('employee-absent-reason');
                });
            }]
        })
        .state('employee-absent-reason.edit', {
            parent: 'employee-absent-reason',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/employee-absent-reason/employee-absent-reason-dialog.html',
                    controller: 'EmployeeAbsentReasonDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['EmployeeAbsentReason', function(EmployeeAbsentReason) {
                            return EmployeeAbsentReason.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('employee-absent-reason', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('employee-absent-reason.delete', {
            parent: 'employee-absent-reason',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/employee-absent-reason/employee-absent-reason-delete-dialog.html',
                    controller: 'EmployeeAbsentReasonDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['EmployeeAbsentReason', function(EmployeeAbsentReason) {
                            return EmployeeAbsentReason.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('employee-absent-reason', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
