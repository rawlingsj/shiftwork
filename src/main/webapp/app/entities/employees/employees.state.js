(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('employees', {
            parent: 'entity',
            url: '/employees2',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'shiftworkApp.employees.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/employees/employees.html',
                    controller: 'EmployeesController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('employees');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('employees-detail', {
            parent: 'entity',
            url: '/employees2/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'shiftworkApp.employees.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/employees/employees-detail.html',
                    controller: 'EmployeesDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('employees');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Employees', function($stateParams, Employees) {
                    return Employees.get({id : $stateParams.id});
                }]
            }
        })
        .state('employees.new', {
            parent: 'employees',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/employees/employees-dialog.html',
                    controller: 'EmployeesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('employees', null, { reload: true });
                }, function() {
                    $state.go('employees');
                });
            }]
        })
        .state('employees.edit', {
            parent: 'employees',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/employees/employees-dialog.html',
                    controller: 'EmployeesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Employees', function(Employees) {
                            return Employees.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('employees', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('employees.delete', {
            parent: 'employees',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/employees/employees-delete-dialog.html',
                    controller: 'EmployeesDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Employees', function(Employees) {
                            return Employees.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('employees', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
