var TH = angular.module('trainingHelper');

TH.controller('PanelController', function ($scope, $location, $rootScope, CONF, PanelService) {

    $scope.service = PanelService;

    $rootScope.$on(CONF.EVENT.SELECT_PANEL_EVENT, function(event, args) {
        PanelService.selectPanel(args["panelId"]);
    });

    $rootScope.$on(CONF.EVENT.LOGIN_STATUS_CHANGE, function(event,status) {

        PanelService.panelVisible("create", status.logged);
        PanelService.panelVisible("createDoc", status.logged);
        PanelService.panelVisible("edit", status.logged);

    });

    PanelService.selectPanel("home");
});