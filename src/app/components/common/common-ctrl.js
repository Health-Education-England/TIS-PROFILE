/* globals angular */

/**
 * @desc - this controller acts as a main parent controller for the app that all nested child controllers have access
 to. It's primary purpose is to $watch the currentTrainee status on the TraineeService and set an 'isUpdating' property
 on the common controller. This is to signal to the user that the app is updating whilst used in conjunction with a
 loading spinner.

 This common controller also loads the common translation json file via the $translate service and initializes
 the user's permissions based on the roles
 */

'use strict';

angular.module('heeTisGuiApp')
	.controller('commonCtrl',
		[ '$scope', '$rootScope', '$location', '$window', '$cookies', '$cookieStore', '$translate', '$stateParams',
			'$translatePartialLoader', 'LoginService', 'authz', '$state',

			function ($scope, $rootScope, $location, $window, $cookies, $cookieStore, $translate, $stateParams,
					$translatePartialLoader, LoginService, authz, $state) {

				var ctrl = this;
				ctrl.isUpdating = false;
				ctrl.init = false;

				$rootScope.$state = $state;

				// set fullscreen variable on $rootscope based on $stateParams
				$scope.$watch(function() {
					return $state.params.fullScreen;
				}, function(newValue) {
					if(newValue === 'full-screen') {
						$rootScope.fullScreen = true;
					} else {
						$rootScope.fullScreen = false;
					}
				});

				ctrl.popOut = function() {
					$stateParams.fullScreen = 'full-screen';
					var url = $state.href($state.current.name);

					$window.open(url, '', 'width=1024,height=600,top=200,left=200,toolbar=no');
				};

				// If user accesses a page directly http://host:8080/#/revalidation-list without logging in, redirect him to login page
				if (!$cookieStore.get('user') || $cookieStore.get('user').loggedIn !== true) {
					$state.go('login');
				} else {
					$rootScope.user = $cookieStore.get('user');
					authz.setPermissions($rootScope.user.permissions);
					// ctrl.checkPermissions($rootScope.user);
				}

				ctrl.print = function () {
					$window.print();
				};

				$translatePartialLoader.addPart('common');
				$translate.refresh();
			}]);
