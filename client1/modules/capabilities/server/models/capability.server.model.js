'use strict';

var mongoose = require ('mongoose'),
	helpers = require(require('path').resolve('./modules/core/server/controllers/core.server.helpers')),
	Schema = mongoose.Schema;

var SubscriptionsSchema = new Schema ({
	event            : {type: String, default: ''},
	callback         : {type: String, default: ''},
	secret           : {type: String, default: ''}
});

CapabilitySchema.statics.findUniqueCode = function (title, suffix, callback) {
	return helpers.modelFindUniqueCode (this, 'capability', title, suffix, callback);
};
CapabilitySkillSchema.statics.findUniqueCode = function (title, suffix, callback) {
	return helpers.modelFindUniqueCode (this, 'capabilityskill', title, suffix, callback);
};

mongoose.model ('Capability', CapabilitySchema);
mongoose.model ('CapabilitySkill', CapabilitySkillSchema);
