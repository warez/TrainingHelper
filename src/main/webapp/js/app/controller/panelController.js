var TH = angular.module('trainingHelper');

TH.controller('PanelController', function ($scope, $location, $rootScope, CONF) {

    $scope.panels = [
        {id: "home", href:"/", label:'Home',active: true, dropdown: false},
        {id: "create", href:"create", label:'Crea allenamento',active: false, dropdown: false},
        {id: "edit", href:"#", label:'Modifica allenamento',active: false, dropdown: true},
        {id: "createDoc", href:"createDoc", label:'Esporta allenamenti',active: false, dropdown: false},
        {id: "contact", href:"contact", label:'Contatti',active: false, dropdown: false},
    ];

    $scope.selectPanel = function(arg) {

        var test = function(panel) {
            return arg.id === panel.id;
        };

        if(typeof arg === "string") {
            test = function(panel) {
                return arg == panel.id;
            }
        };

        for(var i = 0 ; i< $scope.panels.length; i++) {
            $scope.panels[i].active = test($scope.panels[i]);
        }
    };

    $rootScope.$on(CONF.EVENT.SELECT_PANEL_EVENT, function(event, args) {
        $scope.selectPanel(args["panelId"]);
    });

    $scope.selectPanel("create");
});