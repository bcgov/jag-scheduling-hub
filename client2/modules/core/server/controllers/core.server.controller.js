'use strict';

var validator = require('validator'),
  path = require('path'),
  config = require(path.resolve('./config/config'));

/**
 * Render the main application page
 */
 // CC:USERFIELDS
exports.renderIndex = function (req, res) {
  var safeUserObject = null;
  if (req.user) {
    safeUserObject = {
      _id                     : req.user._id,
      displayName             : validator.escape(req.user.displayName),
      provider                : validator.escape(req.user.provider),
      username                : validator.escape(req.user.username),
      created                 : req.user.created.toString(),
      roles                   : req.user.roles,
      profileImageURL         : req.user.profileImageURL,
      email                   : validator.escape(req.user.email),
      lastName                : validator.escape(req.user.lastName),
      firstName               : validator.escape(req.user.firstName),
      additionalProvidersData : req.user.additionalProvidersData
    };
  }
  var features = config.features.split ('-');
  var featureObject = {};
  features.forEach (function (el) {
    featureObject[el] = true;
  });

  res.render('modules/core/server/views/index', {
    user: JSON.stringify(safeUserObject),
    sharedConfig: JSON.stringify(config.shared),
    //
    // CC:FEATURES
    //
    // in prod, config.feature_hide will be true. in dev and test false
    // use this fact to hide and show features in the front end
    //
    // feature_hide: config.feature_hide
    features: JSON.stringify (featureObject)
  });
};

/**
 * Render the server error page
 */
exports.renderServerError = function (req, res) {
  res.status(500).render('modules/core/server/views/500', {
    error: 'Oops! Something went wrong...'
  });
};

/**
 * Render the server not found responses
 * Performs content-negotiation on the Accept HTTP header
 */
exports.renderNotFound = function (req, res) {

  res.status(404).format({
    'text/html': function () {
      res.render('modules/core/server/views/404', {
        url: req.originalUrl
      });
    },
    'application/json': function () {
      res.json({
        error: 'Path not found'
      });
    },
    'default': function () {
      res.send('Path not found');
    }
  });
};

exports.getFlags = function () {
  return {
    featureHide: config.feature_hide
  };
};
