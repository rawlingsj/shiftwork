(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('view-day-plan', {
            parent: 'entity',
            url: '/view-day-plan',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'shiftworkApp.viewDayPlan.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/view-day-plan/view-day-plan.html',
                    controller: 'ViewDayPlanController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('viewDayPlan');
                    return $translate.refresh();
                }]
            }
        });
    }

})();
