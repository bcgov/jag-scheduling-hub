'use strict';

var mongoose = require ('mongoose'),
	helpers = require(require('path').resolve('./modules/core/server/controllers/core.server.helpers')),
	Schema = mongoose.Schema;

// -------------------------------------------------------------------------
//
// Subscribers
//
// -------------------------------------------------------------------------
var SubscriberSchema = new Schema ({
	name       : {type: String, default: ''},
	subscriber : {type: Schema.ObjectId, ref: 'Subscriber'}},
});
// -------------------------------------------------------------------------
//
// Events
//
// -------------------------------------------------------------------------
var HubEventSchema = new Schema ({
	code        : {type: String, default: ''},
	name        : {type: String, default: ''},
	description : {type: String, default: ''},
	secret      : {type: String, default: ''}
});
// -------------------------------------------------------------------------
//
// Subscriptions
//
// -------------------------------------------------------------------------
var SubscriptionSchema = new Schema ({
	event      : {type: Schema.ObjectId, ref: 'HubEvent'}},
	subscriber : {type: Schema.ObjectId, ref: 'Subscriber'}},
	callback   : {type: String, default: ''}
});

mongoose.model ('Event', EventSchema);
mongoose.model ('EventSkill', EventSkillSchema);
