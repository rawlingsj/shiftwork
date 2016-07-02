(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('pattern-detail', {
            parent: 'entity',
            url: '/pattern/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'shiftworkApp.pattern.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/pattern/pattern-detail.html',
                    controller: 'PatternDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('pattern');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Pattern', function($stateParams, Pattern) {
                    return Pattern.get({id : $stateParams.id});
                }]
            }
        });
    }

})();
