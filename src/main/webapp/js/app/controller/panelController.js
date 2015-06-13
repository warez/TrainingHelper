var TH = angular.module('trainingHelper');

TH.controller('PanelController', function ($scope, $location, $rootScope, CONF, PanelService) {

    $scope.panels = PanelService.getPanels();
    $scope.service = PanelService;

    $rootScope.$on(CONF.EVENT.SELECT_PANEL_EVENT, function(event, args) {
        PanelService.selectPanel($scope.panels, args["panelId"]);
    });

    $rootScope.$on(CONF.EVENT.LOGIN_STATUS_CHANGE, function(event,status) {

        PanelService.panelVisible($scope.panels, "create", status["logged"] );
        PanelService.panelVisible($scope.panels, "createDoc", status["logged"] );
        PanelService.panelVisible($scope.panels, "edit", status["logged"] );

    });

    $rootScope.$on(CONF.EVENT.TRAINING_LOADED, function(event,ev) {

        PanelService.panelVisible($scope.panels, "edit", ev["trainingCount"] > 0);
        PanelService.panelVisible($scope.panels, "createDoc", ev["trainingCount"] > 0);

    });

    PanelService.selectPanel($scope.panels, "home");
});