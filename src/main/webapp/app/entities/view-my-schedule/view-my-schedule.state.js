(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('view-my-schedule', {
            parent: 'entity',
            url: '/view-my-schedule',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'shiftworkApp.viewMySchedule.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/view-my-schedule/view-my-schedule.html',
                    controller: 'ViewMyScheduleController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
					$translatePartialLoader.addPart('viewMySchedule');
					$translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
		.state('view-my-schedule-detail', {
            parent: 'view-my-schedule',
            url: '/view-my-schedule-detail',
			params : {
				data : null
			},
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
				if($stateParams.data == null) {
					$state.go("view-my-schedule");
					return;
				}
                $uibModal.open({
                    templateUrl: 'app/entities/view-my-schedule/view-my-schedule-detail.html',
                    controller: 'ViewMyScheduleDetailController',
                    controllerAs: 'vm',
                    size: 'lg'
                }).result.then(function() {
                    $state.go('view-my-schedule', null, { reload: true });
                }, function() {
                    $state.go('view-my-schedule');
                });
            }]
        });
    }

})();
