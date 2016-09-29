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
		'ngAnimate',
		'ngResource',
		'ngSanitize',
		'ngTouch',
		'ngCookies',
		'ngBiscuit',
		'angular-authz',
		'heeTemplates',
		'pascalprecht.translate',
		'ui.select',
		'ui.router'
	])
	.config(function ($stateProvider, $urlRouterProvider, $translateProvider) {
		$urlRouterProvider.otherwise('/login');

		$stateProvider
			// login
			.state('login', {
				templateUrl: 'app/components/login/login.html',
				url: '/login'
			})

			// logout
			.state('logout', {
			    templateUrl: 'app/components/login/login.html',
                url: '/logout'
            })
			// not authorized
			.state('notAuthorized', {
				templateUrl: 'app/components/not-authorized/not-authorized.html',
				url: '/not-authorized'
			});

		$translateProvider.useLoader('$translatePartialLoader', {
			urlTemplate: 'i18n/{part}/{part}.i18n.{lang}.json'
		});

		$translateProvider.use('en');

		// Enable escaping of HTML
		$translateProvider.useSanitizeValueStrategy('sanitize');

	})
	.constant('PERMISSIONS', {
		SEE_ALL_TRAINEES: 'see:all:trainees',
		SEE_RO_TRAINEES: 'see:ro:trainees',
		ACTION_TRAINEE_WORKFLOW: 'action:trainee:workflow',
		SUBMIT_TO_RO: 'submit:to:ro',
		SUBMIT_TO_GMC: 'submit:to:gmc',
		CONTACT_DETAILS_SELF: 'contact:details:self'
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
	});
