/* globals angular */
'use strict';

angular.module('heeTisGuiApp')
	.controller('LoginCtrl', ['$translate', '$translatePartialLoader', 'LoginService', 'PermissionsService', '$cookies',
	'cookieStore', '$rootScope', 'ROLES','$location', '$state', '$window', '$timeout',

	function ($translate, $translatePartialLoader, LoginService, PermissionsService, $cookies, cookieStore, $rootScope,
	ROLES, $location, $state, $window, $timeout) {

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
				// Prepare logged in user and set it in route scope
				var fullName = response.fullName;
				var uid = response.userName;
				var roles = [];

				response.roles.forEach(function(roleString){
					if (roleString && roleString !== ''){
						roles.push(roleString);
					}
				});

				var isAdmin = roles.indexOf(ROLES.ADMIN) !== -1;
				var isOfficer = roles.indexOf(ROLES.OFFICER) !== -1;
				var isTrainee = roles.indexOf(ROLES.TRAINEE) !== -1;

				var token = response.tokenId;

				var user = { name: fullName, uid: uid, roles: roles, permissions: [],
					isAdmin: isAdmin, isOfficer: isOfficer, isTrainee: isTrainee, token: token, loggedIn: true};

				user.permissions = response.permissions;
				$rootScope.user = user;
				ctrl.checkPermissions(user);
			}, function(response) {
				console.log(response.errorMessage);

				ctrl.showLoginFeedback = true;

				ctrl.loginFeedback = {
					state: 'alert-danger',
					message: 'LOGIN.LOGIN_FAILURE'
				};

				$timeout(function () {
					ctrl.loginFeedback = null;
				}, 2000);
			});
		};

		ctrl.checkPermissions = function(user) {
		    var appUrl = '//' + $location.host() + '/revalidation/';
		    console.log('Redirecting to: '+ appUrl);
            cookieStore.put('user', JSON.stringify(user), { path: "/" });
            if ($location.url() === "" || $location.url() === "/login" || $location.url() === "/") {
                $window.location.replace(appUrl);
            } else {
                $state.reload();
            }
		};

		$translatePartialLoader.addPart('login');
		$translate.refresh();
	}]);
