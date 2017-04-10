// Karma configuration
// Generated on 2016-07-21

module.exports = function(config) {
	'use strict';

	config.set({
		// enable / disable watching file and executing tests whenever any file changes
		autoWatch: true,

		// base path, that will be used to resolve files and exclude
		basePath: '',

		// testing framework to use (jasmine/mocha/qunit/...)
		// as well as any additional frameworks (requirejs/chai/sinon/...)
		frameworks: [
			'jasmine'
		],

		// list of files / patterns to load in the browser
		files: [
			'js/bower_components/jquery/dist/jquery.min.js',
			'js/bower_components/jquery-ui/jquery-ui.js',
			'js/bower_components/angular/angular.min.js',
			'js/bower_components/angular-animate/angular-animate.min.js',
			'js/bower_components/angular-cookies/angular-cookies.min.js',
			'js/bower_components/angular-resource/angular-resource.min.js',
			'js/bower_components/angular-sanitize/angular-sanitize.min.js',
			'js/bower_components/angular-touch/angular-touch.min.js',
			'js/bower_components/angular-translate/angular-translate.min.js',
			'js/bower_components/angular-authz/dist/angular-authz.js',
			'js/bower_components/angular-translate-loader-partial/angular-translate-loader-partial.min.js',
			'js/bower_components/angular-ui-select/dist/select.js',
			'js/bower_components/angular-ui-router/release/angular-ui-router.js',
			'js/bower_components/angular-mocks/angular-mocks.js',
			'js/app/scripts/tests/global-variables.spec.js',
			'js/app/scripts/app.js',
			'js/app/scripts/**/*.js',
			'js/app/components/**/*.js'
		],

		// list of files / patterns to exclude
		exclude: [
		],

		// web server port
		port: 8080,

		// Start these browsers, currently available:
		// - Chrome
		// - ChromeCanary
		// - Firefox
		// - Opera
		// - Safari (only Mac)
		// - PhantomJS
		// - IE (only Windows)
		browsers: [
			'PhantomJS'
		],

		// Which plugins to enable
		plugins: [
			'karma-phantomjs-launcher',
			'karma-jasmine',
			'karma-coverage'
		],

		reporters: [
			'progress',
			'coverage'
		],

		coverageReporter: {
			type: 'html',
			dir: 'js/tests/coverage'
		},

		preprocessors: {
			'**/js/app/*.js': ['coverage'],
			'**/js/app/scripts/*.js': ['coverage'],
			'**/js/app/scripts/services/*.js': ['coverage'],
			'**/js/app/components/*/*.js': ['coverage'],
			'**/js/app/components/common/*/*.js': ['coverage']
		},

		// Continuous Integration mode
		// if true, it capture browsers, run tests and exit
		singleRun: false,

		colors: true,

		// level of logging
		// possible values: LOG_DISABLE || LOG_ERROR || LOG_WARN || LOG_INFO || LOG_DEBUG
		logLevel: config.LOG_INFO

		// Uncomment the following lines if you are using grunt's server to run the tests
		// proxies: {
		//   '/': 'http://localhost:9000/'
		// },
		// URL root prevent conflicts with the site root
		// urlRoot: '_karma_'
	});
};
