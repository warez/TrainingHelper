var TH_CONF = angular.module('trainingHelperConf', []);

TH_CONF.constant("CONF", {

    APP_NAME : "TrainingHelper",
    APP_VERSION: "(v 1.0)",
    EVENT: {
        PANEL_VISIBILITY_CHANGE_EVENT: "PANEL_VISIBILITY_CHANGE_EVENT",
        PANEL_SELECTED_EVENT: "PANEL_SELECTED_EVENT",
        SELECT_PANEL_EVENT : "SELECT_PANEL_EVENT",
        TRAINING_DELETE : "TRAINING_DELETE",
        LOGIN_STATUS_CHANGE: "LOGIN_STATUS_CHANGE"
    }

} );