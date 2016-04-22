(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('employee-shift-off-request', {
            parent: 'entity',
            url: '/employee-shift-off-request',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'shiftworkApp.employeeShiftOffRequest.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/employee-shift-off-request/employee-shift-off-requests.html',
                    controller: 'EmployeeShiftOffRequestController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('employeeShiftOffRequest');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('employee-shift-off-request-detail', {
            parent: 'entity',
            url: '/employee-shift-off-request/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'shiftworkApp.employeeShiftOffRequest.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/employee-shift-off-request/employee-shift-off-request-detail.html',
                    controller: 'EmployeeShiftOffRequestDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('employeeShiftOffRequest');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'EmployeeShiftOffRequest', function($stateParams, EmployeeShiftOffRequest) {
                    return EmployeeShiftOffRequest.get({id : $stateParams.id});
                }]
            }
        })
        .state('employee-shift-off-request.new', {
            parent: 'employee-shift-off-request',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/employee-shift-off-request/employee-shift-off-request-dialog.html',
                    controller: 'EmployeeShiftOffRequestDialogController',
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
                    $state.go('employee-shift-off-request', null, { reload: true });
                }, function() {
                    $state.go('employee-shift-off-request');
                });
            }]
        })
        .state('employee-shift-off-request.edit', {
            parent: 'employee-shift-off-request',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/employee-shift-off-request/employee-shift-off-request-dialog.html',
                    controller: 'EmployeeShiftOffRequestDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['EmployeeShiftOffRequest', function(EmployeeShiftOffRequest) {
                            return EmployeeShiftOffRequest.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('employee-shift-off-request', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('employee-shift-off-request.delete', {
            parent: 'employee-shift-off-request',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/employee-shift-off-request/employee-shift-off-request-delete-dialog.html',
                    controller: 'EmployeeShiftOffRequestDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['EmployeeShiftOffRequest', function(EmployeeShiftOffRequest) {
                            return EmployeeShiftOffRequest.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('employee-shift-off-request', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
