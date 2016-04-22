(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('task-tpye', {
            parent: 'entity',
            url: '/task-tpye',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'shiftworkApp.taskTpye.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/task-tpye/task-tpyes.html',
                    controller: 'TaskTpyeController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('taskTpye');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('task-tpye-detail', {
            parent: 'entity',
            url: '/task-tpye/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'shiftworkApp.taskTpye.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/task-tpye/task-tpye-detail.html',
                    controller: 'TaskTpyeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('taskTpye');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'TaskTpye', function($stateParams, TaskTpye) {
                    return TaskTpye.get({id : $stateParams.id});
                }]
            }
        })
        .state('task-tpye.new', {
            parent: 'task-tpye',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/task-tpye/task-tpye-dialog.html',
                    controller: 'TaskTpyeDialogController',
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
                    $state.go('task-tpye', null, { reload: true });
                }, function() {
                    $state.go('task-tpye');
                });
            }]
        })
        .state('task-tpye.edit', {
            parent: 'task-tpye',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/task-tpye/task-tpye-dialog.html',
                    controller: 'TaskTpyeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TaskTpye', function(TaskTpye) {
                            return TaskTpye.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('task-tpye', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('task-tpye.delete', {
            parent: 'task-tpye',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/task-tpye/task-tpye-delete-dialog.html',
                    controller: 'TaskTpyeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['TaskTpye', function(TaskTpye) {
                            return TaskTpye.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('task-tpye', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
