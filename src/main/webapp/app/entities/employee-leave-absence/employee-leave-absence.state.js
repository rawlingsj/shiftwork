(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('employee-leave-absence', {
            parent: 'entity',
            url: '/employee-leave-absence',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'shiftworkApp.employeeLeaveAbsence.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/employee-leave-absence/employee-leave-absences.html',
                    controller: 'EmployeeLeaveAbsenceController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('employeeLeaveAbsence');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('employee-leave-absence-detail', {
            parent: 'entity',
            url: '/employee-leave-absence/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'shiftworkApp.employeeLeaveAbsence.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/employee-leave-absence/employee-leave-absence-detail.html',
                    controller: 'EmployeeLeaveAbsenceDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('employeeLeaveAbsence');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'EmployeeLeaveAbsence', function($stateParams, EmployeeLeaveAbsence) {
                    return EmployeeLeaveAbsence.get({id : $stateParams.id});
                }]
            }
        })
        .state('employee-leave-absence.new', {
            parent: 'employee-leave-absence',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/employee-leave-absence/employee-leave-absence-dialog.html',
                    controller: 'EmployeeLeaveAbsenceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                absentFrom: null,
                                absentTo: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('employee-leave-absence', null, { reload: true });
                }, function() {
                    $state.go('employee-leave-absence');
                });
            }]
        })
        .state('employee-leave-absence.edit', {
            parent: 'employee-leave-absence',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/employee-leave-absence/employee-leave-absence-dialog.html',
                    controller: 'EmployeeLeaveAbsenceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['EmployeeLeaveAbsence', function(EmployeeLeaveAbsence) {
                            return EmployeeLeaveAbsence.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('employee-leave-absence', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('employee-leave-absence.delete', {
            parent: 'employee-leave-absence',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/employee-leave-absence/employee-leave-absence-delete-dialog.html',
                    controller: 'EmployeeLeaveAbsenceDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['EmployeeLeaveAbsence', function(EmployeeLeaveAbsence) {
                            return EmployeeLeaveAbsence.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('employee-leave-absence', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
