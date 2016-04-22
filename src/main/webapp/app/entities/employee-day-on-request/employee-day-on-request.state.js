(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('employee-day-on-request', {
            parent: 'entity',
            url: '/employee-day-on-request',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'shiftworkApp.employeeDayOnRequest.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/employee-day-on-request/employee-day-on-requests.html',
                    controller: 'EmployeeDayOnRequestController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('employeeDayOnRequest');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('employee-day-on-request-detail', {
            parent: 'entity',
            url: '/employee-day-on-request/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'shiftworkApp.employeeDayOnRequest.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/employee-day-on-request/employee-day-on-request-detail.html',
                    controller: 'EmployeeDayOnRequestDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('employeeDayOnRequest');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'EmployeeDayOnRequest', function($stateParams, EmployeeDayOnRequest) {
                    return EmployeeDayOnRequest.get({id : $stateParams.id});
                }]
            }
        })
        .state('employee-day-on-request.new', {
            parent: 'employee-day-on-request',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/employee-day-on-request/employee-day-on-request-dialog.html',
                    controller: 'EmployeeDayOnRequestDialogController',
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
                    $state.go('employee-day-on-request', null, { reload: true });
                }, function() {
                    $state.go('employee-day-on-request');
                });
            }]
        })
        .state('employee-day-on-request.edit', {
            parent: 'employee-day-on-request',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/employee-day-on-request/employee-day-on-request-dialog.html',
                    controller: 'EmployeeDayOnRequestDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['EmployeeDayOnRequest', function(EmployeeDayOnRequest) {
                            return EmployeeDayOnRequest.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('employee-day-on-request', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('employee-day-on-request.delete', {
            parent: 'employee-day-on-request',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/employee-day-on-request/employee-day-on-request-delete-dialog.html',
                    controller: 'EmployeeDayOnRequestDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['EmployeeDayOnRequest', function(EmployeeDayOnRequest) {
                            return EmployeeDayOnRequest.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('employee-day-on-request', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
