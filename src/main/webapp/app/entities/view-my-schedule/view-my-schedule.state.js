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
                    return $translate.refresh();
                }]
            }
        });
    }

})();
