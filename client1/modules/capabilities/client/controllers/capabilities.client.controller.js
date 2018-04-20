(function () {
	'use strict';
	angular.module ('capabilities')
	// =========================================================================
	//
	// Controller the view of the capability page
	//
	// =========================================================================
	.controller ('CapabilityController', function ($scope, $state, Authentication, Notification) {
		var vm        = this;
		vm.trust      = $sce.trustAsHtml;
		vm.capability = capability;
		vm.auth       = Authentication.permissions ();
		vm.canEdit    = vm.auth.isAdmin;
		vm.capability     = capability;
		vm.newskill       = '';
		vm.editingskill   = false;
	})
	;
}());
