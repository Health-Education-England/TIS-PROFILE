/* globals angular */
'use strict';

/**
 * This service provides access to the /permissions API which lists the permissions for a given set
 * of roles
 */
angular.module('heeTisGuiApp')
	.factory('LoginService', ['EndpointFactory', '$state', '$rootScope', '$location', 'cookieStore',
	function(EndpointFactory, $state, $rootScope, $location, cookieStore) {
			return {
				logout: EndpointFactory.connect('identity/logout'),
				authenticateUser: EndpointFactory.connect('identity/authenticate'),
				logoutUser: function() {
				    console.log('logging off !!');
                    var userToken = $rootScope.user.token;
                    cookieStore.remove('user', { path: "/" });
                    $rootScope.user = undefined;
                    this.logout.create({
                        headers: { 'X-TIS-TokenId': userToken}
                    }).then(function(response) {
                        console.log('logout response: '+response);
                        $state.go('login');
                    }, function() {
                        $state.go('login');
                    });
                }
			};
		}]);
