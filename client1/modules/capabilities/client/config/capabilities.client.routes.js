// =========================================================================
//
// All the client side routes for capabilities
//
// =========================================================================
(function () {
	'use strict';
	angular.module ('capabilities.routes').config (['$stateProvider', function ($stateProvider) {
		$stateProvider
		// -------------------------------------------------------------------------
		//
		// this is the top level, abstract route for all capability routes, it only
		// contians the ui-view that all other routes get rendered in
		//
		// -------------------------------------------------------------------------
		.state ('capabilities', {
			url      : '/capabilities',
			template : '/modules/capabilities/client/views/view-capability.client.view.html',
			controller   : 'CapabilityController',
			controllerAs : 'vm',
			resolve: {
				capability: function ($stateParams, CapabilitiesService) {
					return CapabilitiesService.get ({
						capabilityId: $stateParams.capabilityId
					}).$promise;
				}
			},
			data: {
				pageTitle: 'Capability: {{ capability.name }}'
			}
		})
		;
	}]);
}());


