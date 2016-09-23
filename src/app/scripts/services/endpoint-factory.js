/* globals angular */
'use strict';

angular.module('heeTisGuiApp')
	.factory('EndpointFactory', ['$http', '$q', '$location',
	function ($http, $q, $location) {

		var Endpoints = function(endpoint) {

			var self = this;
			var methods = ['GET', 'POST', 'PUT', 'PATCH', 'DELETE'];
			var hostFromEnv = $location.host();
			var base = 'http://' + hostFromEnv + ':8080/';

			var _endpoints = {};
			// var version = 'v1';

			angular.forEach(methods, function(method) {
				_endpoints[method.toLowerCase()] = function(opts) {
					opts.url = base + endpoint;

					if (opts.parts) {
						opts.url = opts.url.split('/').map(function(section) {
							if (section.substr(0, 1) === ':') {
								if (angular.isDefined(opts.parts[section.substr(1)])) {
									section = opts.parts[section.substr(1)];
								} else {
									throw new Error('no corresponding replacement part for ' + section);
								}
							}
							return section;
						}).join('/');
					}

					var params = [];
					for (var p in opts.params) {
						if (opts.params.hasOwnProperty(p)) {
							params.push(p + '=' + opts.params[p]);
						}
					}
					opts.url = opts.url + '?' + params.join('&');

					$http({
						url: opts.url,
						data: opts.payload,
						headers: opts.headers,
						method: method
					})
					.success(function(response) {
						opts.success(response);
					})
					.error(function(response) {
						opts.failure(response);
					});

				};
			});

			self.fetch = function(opts) {

				if (!opts) {
					return false;
				}

				var deferred = $q.defer();

				opts.success = function(response) {
					deferred.resolve(response);
				};

				opts.failure = function(response) {
					deferred.reject(response);
				};

				_endpoints.get(opts);

				return deferred.promise;

			};

			self.create = function(opts) {

				if (!opts) {
					return false;
				}

				var deferred = $q.defer();

				opts.url = endpoint;

				opts.success = function(response) {
					deferred.resolve(response);
				};

				opts.failure = function(response) {
					deferred.reject(response);
				};

				_endpoints.post(opts);

				return deferred.promise;

			};

			self.update = function(opts) {

				if (!opts) {
					return false;
				}

				var deferred = $q.defer();

				opts.success = function(response) {
					deferred.resolve(response);
				};

				opts.failure = function(response) {
					deferred.reject(response);
				};

				_endpoints.put(opts);

				return deferred.promise;

			};

			self.amend = function(opts) {

				if (!opts) {
					return false;
				}

				var deferred = $q.defer();

				opts.success = function(response) {
					deferred.resolve(response);
				};

				opts.failure = function(response) {
					deferred.reject(response);
				};

				_endpoints.patch(opts);

				return deferred.promise;

			};

			self.remove = function(opts) {

				if (!opts) {
					return false;
				}

				var deferred = $q.defer();

				opts.success = function(response) {
					deferred.resolve(response);
				};

				opts.failure = function(response) {
					deferred.reject(response);
				};

				_endpoints['delete'](opts);

				return deferred.promise;

			};

			return self;

		};

		return {
			connect: function(endpoint) {
				return new Endpoints(endpoint);
			}
		};

	}]);
