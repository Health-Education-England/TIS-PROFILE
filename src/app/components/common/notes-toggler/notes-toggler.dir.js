/* globals angular */

/**
	* @desc - this directive renders out the markup for the notes toggler element. Its purpose is to show/hide visibility
	* of the notes column by animating the widths of the center and right column [notes column]
*/

'use strict';

angular.module('heeTisGuiApp')
	.directive('notesToggler', function() {

		return {
			restrict: 'E', //E = element, A = attribute, C = class, M = comment
			templateUrl: 'app/components/common/notes-toggler/notes-toggler.inc.html',
			link: function() {
				var isIE8 = angular.element('body').hasClass('ie8'),
					leftColumn = angular.element('.left-column'),
					centerColumn = angular.element('.center-column'),
					notesColumn = angular.element('.notes-column'),
					notesToggler = angular.element('.notes-toggler'),
					notesTogglerArrow = angular.element('.notes-toggler .glyphicon'),
					duration = 300;

				notesToggler.click(function(){

					// cater for IE8's lack of min-width support
					if(isIE8){
						leftColumn.css({ 'width': '25%' });
					} else {
						leftColumn.css({ 'min-width': '25%' });
					}

					// hide trainee notes
					if(!notesColumn.hasClass('show-notes')){
						centerColumn.animate({ width: '75%' }, duration);

						notesColumn.animate({
							width: '0%',
							paddingLeft: '0',
							paddingRight: '0'
						}, duration, function(){
							notesColumn.addClass('show-notes');
						});
					}
					// show trainee notes
					else {
						centerColumn.animate({ width: '50%' }, duration);

						notesColumn.animate({
							width: '25%',
							paddingLeft: '15px',
							paddingRight: '15px'
						}, duration, function(){
							notesColumn.removeClass('show-notes');
							notesColumn.removeAttr('style');
							centerColumn.removeAttr('style');
						});
					}

					notesTogglerArrow.toggleClass('glyphicon-arrow-right glyphicon-arrow-left');

				});
			}
		};
	});
