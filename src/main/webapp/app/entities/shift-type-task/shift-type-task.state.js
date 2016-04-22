(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('shift-type-task', {
            parent: 'entity',
            url: '/shift-type-task',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'shiftworkApp.shiftTypeTask.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/shift-type-task/shift-type-tasks.html',
                    controller: 'ShiftTypeTaskController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('shiftTypeTask');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('shift-type-task-detail', {
            parent: 'entity',
            url: '/shift-type-task/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'shiftworkApp.shiftTypeTask.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/shift-type-task/shift-type-task-detail.html',
                    controller: 'ShiftTypeTaskDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('shiftTypeTask');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ShiftTypeTask', function($stateParams, ShiftTypeTask) {
                    return ShiftTypeTask.get({id : $stateParams.id});
                }]
            }
        })
        .state('shift-type-task.new', {
            parent: 'shift-type-task',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/shift-type-task/shift-type-task-dialog.html',
                    controller: 'ShiftTypeTaskDialogController',
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
                    $state.go('shift-type-task', null, { reload: true });
                }, function() {
                    $state.go('shift-type-task');
                });
            }]
        })
        .state('shift-type-task.edit', {
            parent: 'shift-type-task',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/shift-type-task/shift-type-task-dialog.html',
                    controller: 'ShiftTypeTaskDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ShiftTypeTask', function(ShiftTypeTask) {
                            return ShiftTypeTask.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('shift-type-task', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('shift-type-task.delete', {
            parent: 'shift-type-task',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/shift-type-task/shift-type-task-delete-dialog.html',
                    controller: 'ShiftTypeTaskDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ShiftTypeTask', function(ShiftTypeTask) {
                            return ShiftTypeTask.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('shift-type-task', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
