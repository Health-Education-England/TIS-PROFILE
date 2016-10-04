describe('Login Controller', function () {

	beforeEach(module('heeTisGuiApp'));

	var loginCtrl,
		rootScope,
		httpBackend,
		cookies,
		timeout;

	describe('loginCtrl', function () {

		beforeEach(inject(function(_$controller_, _$rootScope_, _$httpBackend_, _$timeout_) {
			rootScope = _$rootScope_;
			httpBackend = _$httpBackend_;
			timeout = _$timeout_;
			loginCtrl = _$controller_('loginCtrl', {});
		}));

		describe('should validate username and password', function() {

			var responseConfig = {
				headers: {
					'Set-Cookie': 'HTTP-TIS-TokenId=999'
				}
			};

			beforeEach(function($cookies) {
				httpBackend.expectPOST(BASE_API_URL+'identity/authenticate?',
				function(headers) {
					// check if the header was sent, if it wasn't the expectation won't
					// match the request and the test will fail
					return headers['X-TIS-Username'] ===  'testuser' && headers['X-TIS-Password'] ===  'pwd123';
				 })
				.respond(200, '', responseConfig);
			});

			xit('should authenticate a username and password of a user', function() {
				httpBackend.when('POST', BASE_API_URL+'identity/authenticate?',
				function (headers) {
				   expect(headers['X-TIS-Username']).toBe('testuser');
		   		   expect(headers['X-TIS-Password']).toBe('pwd123');
				   // MUST return boolean
				   return headers['X-TIS-Username'] ===  'testuser' && headers['X-TIS-Password'] ===  'pwd123';
				}).respond(200, '', responseConfig);
				loginCtrl.authenticate('testuser', 'pwd123');
				httpBackend.flush();
				rootScope.$apply();
			});
		});

		describe('should validate invalid username and password', function() {

			xit('should authenticate an invalid username and password of a user', function() {
				httpBackend.when('POST', BASE_API_URL+'identity/authenticate',
				function (headers) {
				   expect(headers['X-TIS-Username']).toBe('testuser');
				   expect(headers['X-TIS-Password']).toBe('pwd123');
				   // MUST return boolean
				   return headers['X-TIS-Username'] ===  'testuser' && headers['X-TIS-Password'] ===  'pwd123';
				}).respond(function (method, url, data, headers) {
				   return [401, '', {}, 'UNAUTHORIZED'];
				});
				loginCtrl.authenticate('testuser', 'pwd123');
				httpBackend.flush();
				rootScope.$apply();
				expect(401);

			});
		});

		describe('should logout', function() {

			xit('should logout successfully', function() {
				httpBackend.when('POST', BASE_API_URL+'identity/logout',
				function (headers) {
				   expect(headers['X-TIS-TokenId']).toBe('999');
				   // MUST return boolean
				   return headers['X-TIS-TokenId'] ===  '999';
				}).respond(function (method, url, data, headers) {
				   return [200, {}, 'OK'];
				});
				loginCtrl.logout();
				httpBackend.flush();
				rootScope.$apply();
				expect(200);
			});
		});

	});

});
