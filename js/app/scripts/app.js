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
	.run(function ($rootScope) {
		$rootScope.applicationReference = '/profile/';
	});
