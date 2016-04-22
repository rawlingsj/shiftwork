(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('employee-shift-on-request', {
            parent: 'entity',
            url: '/employee-shift-on-request',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'shiftworkApp.employeeShiftOnRequest.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/employee-shift-on-request/employee-shift-on-requests.html',
                    controller: 'EmployeeShiftOnRequestController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('employeeShiftOnRequest');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('employee-shift-on-request-detail', {
            parent: 'entity',
            url: '/employee-shift-on-request/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'shiftworkApp.employeeShiftOnRequest.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/employee-shift-on-request/employee-shift-on-request-detail.html',
                    controller: 'EmployeeShiftOnRequestDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('employeeShiftOnRequest');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'EmployeeShiftOnRequest', function($stateParams, EmployeeShiftOnRequest) {
                    return EmployeeShiftOnRequest.get({id : $stateParams.id});
                }]
            }
        })
        .state('employee-shift-on-request.new', {
            parent: 'employee-shift-on-request',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/employee-shift-on-request/employee-shift-on-request-dialog.html',
                    controller: 'EmployeeShiftOnRequestDialogController',
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
                    $state.go('employee-shift-on-request', null, { reload: true });
                }, function() {
                    $state.go('employee-shift-on-request');
                });
            }]
        })
        .state('employee-shift-on-request.edit', {
            parent: 'employee-shift-on-request',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/employee-shift-on-request/employee-shift-on-request-dialog.html',
                    controller: 'EmployeeShiftOnRequestDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['EmployeeShiftOnRequest', function(EmployeeShiftOnRequest) {
                            return EmployeeShiftOnRequest.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('employee-shift-on-request', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('employee-shift-on-request.delete', {
            parent: 'employee-shift-on-request',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/employee-shift-on-request/employee-shift-on-request-delete-dialog.html',
                    controller: 'EmployeeShiftOnRequestDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['EmployeeShiftOnRequest', function(EmployeeShiftOnRequest) {
                            return EmployeeShiftOnRequest.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('employee-shift-on-request', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
