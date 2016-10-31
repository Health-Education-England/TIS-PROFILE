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
		$urlRouterProvider.otherwise('/unauthorised');

		$stateProvider
			// login
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

	});
