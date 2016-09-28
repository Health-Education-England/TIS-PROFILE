/* globals angular */
'use strict';

angular.module('heeTisGuiApp')
	.controller('mastheadCtrl', ['LoginService', '$translate', '$translatePartialLoader',

		function (LoginService, $translate, $translatePartialLoader) {

			var ctrl = this;

			ctrl.logout = function () {
				LoginService.logoutUser();
			};

			$translatePartialLoader.addPart('masthead');
			$translate.refresh();

		}]);
