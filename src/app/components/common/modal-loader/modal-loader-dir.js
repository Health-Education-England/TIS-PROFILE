/* globals angular */

/**
	* @desc - this directive renders out markup for a modal loader. For layout this directive toggles classes to
		the parent and next dom element in order to achieve a cross browser compatible ui
	* @usage - include and invoke the <modal-loader> directive just before and outside the element you wish to apply
		the modal loader to
	* @params string trigger - the hook on which the modal loader uses to display/hide and add/remove classes
*/

'use strict';

angular.module('heeTisGuiApp')
	.directive('modalLoader', function() {

	return {
		restrict: 'E',
		templateUrl: 'app/components/common/modal-loader/modal-loader.inc.html',
		scope: {
			trigger: '='
		},
		link: function($scope, element) {
			$scope.mapElements = function(){
				$scope.loadingContainer = angular.element(element).parent();
				$scope.loadingContent = angular.element(element).next();
			};

			$scope.addClasses = function(){
				$scope.loadingContainer.addClass('loading-container');
				$scope.loadingContent.addClass('loading-content');
			};

			$scope.removeClasses = function(){
				$scope.loadingContainer.removeClass('loading-container');
				$scope.loadingContent.removeClass('loading-content');
			};

			$scope.mapElements();

			$scope.$watch(function() {
				return $scope.trigger;
			}, function(newValue){
				if(newValue === true){
					$scope.mapElements();
					$scope.addClasses();
				}
				else if(newValue === false){
					$scope.removeClasses();
				}
			});
		}
	};
});
