(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('employee-day-off-request', {
            parent: 'entity',
            url: '/employee-day-off-request',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'shiftworkApp.employeeDayOffRequest.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/employee-day-off-request/employee-day-off-requests.html',
                    controller: 'EmployeeDayOffRequestController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('employeeDayOffRequest');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('employee-day-off-request-detail', {
            parent: 'entity',
            url: '/employee-day-off-request/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'shiftworkApp.employeeDayOffRequest.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/employee-day-off-request/employee-day-off-request-detail.html',
                    controller: 'EmployeeDayOffRequestDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('employeeDayOffRequest');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'EmployeeDayOffRequest', function($stateParams, EmployeeDayOffRequest) {
                    return EmployeeDayOffRequest.get({id : $stateParams.id});
                }]
            }
        })
        .state('employee-day-off-request.new', {
            parent: 'employee-day-off-request',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/employee-day-off-request/employee-day-off-request-dialog.html',
                    controller: 'EmployeeDayOffRequestDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                weight: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('employee-day-off-request', null, { reload: true });
                }, function() {
                    $state.go('employee-day-off-request');
                });
            }]
        })
        .state('employee-day-off-request.edit', {
            parent: 'employee-day-off-request',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/employee-day-off-request/employee-day-off-request-dialog.html',
                    controller: 'EmployeeDayOffRequestDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['EmployeeDayOffRequest', function(EmployeeDayOffRequest) {
                            return EmployeeDayOffRequest.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('employee-day-off-request', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('employee-day-off-request.delete', {
            parent: 'employee-day-off-request',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/employee-day-off-request/employee-day-off-request-delete-dialog.html',
                    controller: 'EmployeeDayOffRequestDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['EmployeeDayOffRequest', function(EmployeeDayOffRequest) {
                            return EmployeeDayOffRequest.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('employee-day-off-request', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
