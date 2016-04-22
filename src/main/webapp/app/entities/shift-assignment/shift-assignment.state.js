(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('shift-assignment', {
            parent: 'entity',
            url: '/shift-assignment',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'shiftworkApp.shiftAssignment.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/shift-assignment/shift-assignments.html',
                    controller: 'ShiftAssignmentController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('shiftAssignment');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('shift-assignment-detail', {
            parent: 'entity',
            url: '/shift-assignment/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'shiftworkApp.shiftAssignment.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/shift-assignment/shift-assignment-detail.html',
                    controller: 'ShiftAssignmentDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('shiftAssignment');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ShiftAssignment', function($stateParams, ShiftAssignment) {
                    return ShiftAssignment.get({id : $stateParams.id});
                }]
            }
        })
        .state('shift-assignment.new', {
            parent: 'shift-assignment',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/shift-assignment/shift-assignment-dialog.html',
                    controller: 'ShiftAssignmentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                indexInShift: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('shift-assignment', null, { reload: true });
                }, function() {
                    $state.go('shift-assignment');
                });
            }]
        })
        .state('shift-assignment.edit', {
            parent: 'shift-assignment',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/shift-assignment/shift-assignment-dialog.html',
                    controller: 'ShiftAssignmentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ShiftAssignment', function(ShiftAssignment) {
                            return ShiftAssignment.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('shift-assignment', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('shift-assignment.delete', {
            parent: 'shift-assignment',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/shift-assignment/shift-assignment-delete-dialog.html',
                    controller: 'ShiftAssignmentDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ShiftAssignment', function(ShiftAssignment) {
                            return ShiftAssignment.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('shift-assignment', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
