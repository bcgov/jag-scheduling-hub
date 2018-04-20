'use strict';

// =========================================================================
//
// this is intended to be a sort of base class for all crud services in the
// client, just to avoid retyping everything over and over again
//
// =========================================================================
angular.module ('core').factory ('JagExchange', ['$http', '$window', '_', function ($http, $window, _) {
	var JagExchange = function (o) {
		this._init (o);
	};
	_.extend (JagExchange.prototype, {
		// -------------------------------------------------------------------------
		//
		// TODO: initialize the object, set some instance vars
		//
		// -------------------------------------------------------------------------
		_init: function (o) {
			_.bindAll (this);
		},
		// -------------------------------------------------------------------------
		//
		// TODO: logs any security audit information in the response headers to the console
		//
		// -------------------------------------------------------------------------
		debugSecurityAudit: function (res) {
			return;
		},
		// -------------------------------------------------------------------------
		//
		// basic
		//
		// -------------------------------------------------------------------------
		put: function (url, data) {
			return this.talk ('PUT', url, data);
		},
		post: function (url, data) {
			return this.talk ('POST', url, data);
		},
		get: function (url) {
			return this.talk ('GET', url, null);
		},
		delete: function (url) {
			return this.talk ('DELETE', url, null);
		},
		talk: function (method, url, data) {
			var self = this;
			return new Promise (function (resolve, reject) {
				$http ({ method: method, url: url, data: data })
				.then (function (res) {
					self.debugSecurityAudit (res);
					resolve (res.data);
				}).catch (function (res) {
					reject (res.data);
				});
			});
		}
	});
	JagExchange.extend = function (protoProps, staticProps) {
		var parent = this;
		var child;
		if (protoProps && _.has (protoProps, 'constructor')) { child = protoProps.constructor; } else {
			child = function () {
				return parent.apply (this, arguments);
			};
		}
		_.extend (child, parent, staticProps);
		var Surrogate = function () { this.constructor = child; };
		Surrogate.prototype = parent.prototype;
		child.prototype = new Surrogate ();
		if (protoProps) _.extend (child.prototype, protoProps);
		child.__super__ = parent.prototype;
		return child;
	};
	return JagExchange;
}]);
