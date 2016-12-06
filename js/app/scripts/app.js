/* globals angular */
'use strict';

/**
 * @ngdoc overview
 * @name heeTisGuiApp
 * @description
 * # heeTisGuiApp
 *
 * Main module of the application.
 */
angular
	.module('heeTisGuiApp', [
		'ngResource',
		'ngSanitize',
		'ngTouch',
		'ngCookies',
		'ngBiscuit',
		'angular-authz',
		'heeTemplates',
		'pascalprecht.translate',
		'ui.router'
	])
	.config(function ($stateProvider, $urlRouterProvider, $translateProvider) {
		$urlRouterProvider.otherwise('/error');

		$stateProvider
			.state('unauthorised', {
				templateUrl: 'app/components/unauthorised/unauthorised.html',
				url: '/unauthorised'
			})
			.state('error', {
                templateUrl: 'app/components/error/error.html',
                url: '/error'
            });

		$translateProvider.translations('en', {
			//::translations:://
		});
		$translateProvider.preferredLanguage('en');

		// Enable escaping of HTML
		$translateProvider.useSanitizeValueStrategy('sanitize');

	})
	.constant('PERMISSIONS', {
		SEE_ALL_TRAINEES: 'revalidation:see:dbc:trainees'
	})
	.constant('ROLES', {
		TRAINEE: 'Trainee',
		ADMIN: 'RVAdmin',
		OFFICER: 'RVOfficer'
	})
	.constant('CONFIG', {
		NOTIFICATION_TIME: 3000
	})
	.run(function ($rootScope, PERMISSIONS, ROLES) {
		$rootScope.PERMISSIONS = PERMISSIONS;
		$rootScope.ROLES = ROLES;
		$rootScope.applicationReference = '/profile/';
	});
