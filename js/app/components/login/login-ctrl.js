/* globals angular */
'use strict';

angular.module('heeTisGuiApp')
	.controller('LoginCtrl', ['$translate', '$translatePartialLoader', 'LoginService', 'PermissionsService',
	'cookieStore', '$rootScope', 'ROLES','$location', '$window',

	function ($translate, $translatePartialLoader, LoginService, PermissionsService, cookieStore, $rootScope, ROLES,
	$location, $window) {

		var ctrl = this;

		if ($location.url() === "/logout") {
			LoginService.logoutUser();
		}

		ctrl.loginFormSubmit = function () {
			ctrl.loginForm.submitted = true;

			// if client side validation passes then authenticate user
			if(ctrl.loginForm.$valid){
				ctrl.authenticate(ctrl.loginForm.data.username, ctrl.loginForm.data.password);
			}
		};

		ctrl.authenticate = function (username, password) {
			LoginService.authenticateUser.create({headers: { 'X-TIS-Username': username, 'X-TIS-Password':password}})
			.then(function(response) {
				
				var user = response;
				// Prepare logged in user and set it in route scope
				var roles = [];

				response.roles.forEach(function(roleString){
					if (roleString && roleString !== ''){
						roles.push(roleString);
					}
				});
				user.roles = roles;

				user.isAdmin = roles.indexOf(ROLES.ADMIN) !== -1;
				user.isOfficer = roles.indexOf(ROLES.OFFICER) !== -1;
				user.isTrainee = roles.indexOf(ROLES.TRAINEE) !== -1;
				user.loggedIn = true;

				var appUrl = '//' + $location.host() + '/revalidation/';

				// add cookie
				cookieStore.put('user', JSON.stringify(user), { path: "/" });

				// redirect to revalidation app
				$window.location.replace(appUrl);
				console.log('Redirecting to: '+ appUrl);
			}, function(response) {
				ctrl.showLoginFeedback = true;
				ctrl.loginFeedback = { state: 'alert-danger' };

				if(response.status === '500'){
					ctrl.loginFeedback.message = 'LOGIN.INTERNAL_SERVER_ERROR';
				} else {
					ctrl.loginFeedback.message = 'LOGIN.LOGIN_FAILURE';
				}
			});
		};

		$translatePartialLoader.addPart('login');
		$translate.refresh();
	}]);
