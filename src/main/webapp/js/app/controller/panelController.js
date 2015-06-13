var TH = angular.module('trainingHelper');

TH.controller('PanelController', function ($scope, $location, $rootScope, CONF, PanelService) {

    $scope.panels = PanelService.getPanels();

    $rootScope.$on(CONF.EVENT.SELECT_PANEL_EVENT, function(event, args) {
        PanelService.selectPanel(args["panelId"]);
    });

    $rootScope.$on(CONF.EVENT.LOGIN_STATUS_CHANGE, function(event,status) {

        PanelService.panelVisible("create", status["logged"] );
        PanelService.panelVisible("createDoc", status["logged"] );
        PanelService.panelVisible("edit", status["logged"] );

    });

    $rootScope.$on(CONF.EVENT.TRAINING_LOADED, function(event,ev) {

        PanelService.panelVisible("edit", ev["trainingCount"] > 0);
        PanelService.panelVisible("createDoc", ev["trainingCount"] > 0);

    });

    PanelService.selectPanel("home");
});