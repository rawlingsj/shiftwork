(function() {
    'use strict';

    angular
        .module('shiftworkApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('employee-agreement', {
            parent: 'entity',
            url: '/employee-agreement',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'shiftworkApp.employeeAgreement.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/employee-agreement/employee-agreement.html',
                    controller: 'EmployeeAgreementController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('employeeAgreement');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
		.state('new-employee-agreement', {
            parent: 'entity',
            url: '/new-employee-agreement',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'shiftworkApp.newEmployeeAgreement.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/employee-agreement/new-employee-agreement.html',
                    controller: 'NewEmployeeAgreementController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('employeeAgreement');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        });
    }

})();
