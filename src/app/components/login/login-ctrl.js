/* globals angular */
'use strict';

angular.module('heeTisGuiApp')
	.controller('LoginCtrl', ['$translate', '$translatePartialLoader', 'LoginService', 'PermissionsService', '$cookies',
	'$cookieStore', '$rootScope', 'ROLES','$location', '$state', '$window',

	function ($translate, $translatePartialLoader, LoginService, PermissionsService, $cookies, $cookieStore, $rootScope,
	ROLES, $location, $state, $window) {

		var ctrl = this;

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

				$rootScope.user = user;

				ctrl.checkPermissions(user);
			}, function() {
				$state.go('notAuthorized');
			});
		};

		ctrl.checkPermissions = function(user) {
		    var appUrl = 'http://' + $location.host() + '/revalidation';
		    console.log(appUrl);
			PermissionsService.setPermissions(user, function() {
				if ($location.url() === "" || $location.url() === "/login" || $location.url() === "/") {
					$window.location.replace(appUrl);
				} else {
					$state.reload();
				}
				ctrl.init = true;
			});
		};

		$translatePartialLoader.addPart('login');
		$translate.refresh();
	}]);
