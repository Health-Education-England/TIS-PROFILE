/* globals angular */
'use strict';

angular.module('heeTisGuiApp')
	.controller('mastheadCtrl', ['$location', '$window', '$translate', '$translatePartialLoader',

		function ($location, $window, $translate, $translatePartialLoader) {
			var ctrl = this;
			ctrl.logout = function () {
				var logInPageUrl = '//' + $location.host() + '/auth/#/logout';
				$window.location.replace(logInPageUrl);
            };
			$translatePartialLoader.addPart('masthead');
			$translate.refresh();
		}]);
