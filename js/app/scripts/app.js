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
	.config(function ($stateProvider, $urlRouterProvider, $translateProvider, PERMISSIONS) {
		$urlRouterProvider.otherwise('/welcome');

		$stateProvider
			.state('welcome', {
				templateUrl: 'app/components/welcome/welcome.html',
				url: '/welcome',
				controller: 'welcomeCtrl',
				controllerAs: 'ctrl',
				resolve: {
					permission: function(ProfileService) {
						return ProfileService.checkPermission(PERMISSIONS.SEE_ALL_TRAINEES);
					}
				}
			})
			.state('unauthorised', {
				templateUrl: 'app/components/unauthorised/unauthorised.html',
				url: '/unauthorised'
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
