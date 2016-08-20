(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('planning-job', {
            parent: 'entity',
            url: '/planning-job',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'shiftworkApp.planningJob.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/planning-job/planning-jobs.html',
                    controller: 'PlanningJobController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('planningJob');
                    $translatePartialLoader.addPart('jobStatus');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('planning-job-detail', {
            parent: 'entity',
            url: '/planning-job/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'shiftworkApp.planningJob.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/planning-job/planning-job-detail.html',
                    controller: 'PlanningJobDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('planningJob');
                    $translatePartialLoader.addPart('jobStatus');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'PlanningJob', function($stateParams, PlanningJob) {
                    return PlanningJob.get({id : $stateParams.id});
                }]
            }
        })
        .state('planning-job.new', {
            parent: 'planning-job',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/planning-job/planning-job-dialog.html',
                    controller: 'PlanningJobDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                jobId: null,
                                status: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('planning-job', null, { reload: true });
                }, function() {
                    $state.go('planning-job');
                });
            }]
        })
        .state('planning-job.delete', {
            parent: 'planning-job',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/planning-job/planning-job-delete-dialog.html',
                    controller: 'PlanningJobDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['PlanningJob', function(PlanningJob) {
                            return PlanningJob.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('planning-job', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
