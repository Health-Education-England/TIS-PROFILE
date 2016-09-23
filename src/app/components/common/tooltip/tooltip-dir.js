/* globals angular */

/**
	* @desc - this directive listens to the $rootScope for an event and then invokes the bootstrap tooltip.
	Due to the async nature of loading in translations we have to listen for the $translateRefreshEnd event to
	fire on the $rootScope before we can be certain that the translation key to be used in the tooltip is translated.

	* @params string attrs.tooltipContent - this is the translation key which the tooltip uses for its content
*/

'use strict';

angular.module('heeTisGuiApp')
	.directive('tooltip', ['$translate', '$rootScope', function($translate, $rootScope) {

	return {
		restrict: 'A',
		scope: {},
		link: function($scope, element, attrs) {

			if(attrs.tooltipTranslation) {
				$scope.translationID = attrs.tooltipTranslation;

				// listen to $translate event on rootscope
				$rootScope.$on('$translateRefreshEnd', function () {

					// on resolve on translation key show tooltip
					$translate($scope.translationID).then(function (translatedValue) {
						angular.element(element).tooltip({
							title: translatedValue
						});
					});
				});
			}
			else {
				angular.element(element).tooltip({
					title: attrs.tooltipContent
				});
			}
		}
	};
}]);
