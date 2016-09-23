/* globals angular */
'use strict';

/**
 * This service provides access to the /permissions API which lists the permissions for a given set
 * of roles
 */
angular.module('heeTisGuiApp')
	.factory('PermissionsService', ['EndpointFactory', 'authz', '$rootScope', '$state', '$cookieStore',
			function (EndpointFactory, authz, $rootScope, $state, $cookieStore) {
				return {
					permissions: EndpointFactory.connect('permissions'),
					setPermissions: function(user, callback) {
						var roles = user.roles;
						this.permissions.fetch({
							params: {
								roleNames: roles
							}
						}).then(function (resp) {
							var permissions = [];
							roles.forEach(function (role) {
								if (resp.content[role]) {
									resp.content[role].forEach(function (permission) {
										if (permission.name) {
											permissions.push(permission.name);
										}
									});
								}
							});
							authz.setPermissions(permissions);
							user.permissions = permissions;
							$cookieStore.put('user', user);

							//check permissions per URL
							$rootScope.$on('$stateChangeStart', function(event, toState) {
								// check if user is authorized to view page
								if (toState.hasOwnProperty('permission') && !authz.hasPermission(toState.permission)) {
									// prevent the transition from happening
									event.preventDefault();
									$state.go('notAuthorized');
								}
							});

							callback();
						});
					}
				};
			}]);
