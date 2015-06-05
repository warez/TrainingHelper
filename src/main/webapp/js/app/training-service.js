var TH_SERVICE = angular.module('trainingHelperService', ["ngResource"]);

TH_SERVICE.factory("LoginService", function($resource ,CONF, $rootScope, LoginClient) {

    var loginStatus = { logged : false };

    return {

        checkLogin : function() {
            LoginClient.loginInfo({}, function(result) {

                var fireEvent = (loginStatus.logged != result.logged);

                loginStatus.logged = result.logged;
                loginStatus.nickName = result.nickName;
                loginStatus.email = result.email;

                if(fireEvent)
                    $rootScope.$broadcast(CONF.EVENT.LOGIN_STATUS_CHANGE, loginStatus);

            }, function(error) {

                console.info(error);
                loginStatus = { logged : false };
                $rootScope.$broadcast(CONF.EVENT.LOGIN_STATUS_CHANGE, loginStatus);

            });
        }

    };

});

TH_SERVICE.factory("PanelService", function($resource,CONF, $rootScope) {

    var panels = {
        home: {id: "home", href: "/", label: 'Home', active: true, dropdown: false, visible: true},
        create: {id: "create", href: "create", label: 'Crea allenamento', active: false, dropdown: false, visible: false},
        edit: {id: "edit", href: "#", label: 'Modifica allenamento', active: false, dropdown: true, visible: false},
        createDoc: {id: "createDoc", href: "createDoc", label: 'Esporta allenamenti', active: false, dropdown: false, visible: false},
        contact: {id: "contact", href: "contact", label: 'Contatti', active: false, dropdown: false, visible: true}
    };

    return {

        getPanels: function() {
            return panels;
        },

        panelVisible: function(id, visible) {

            if(visible != undefined) {
                panels[id].visible = visible;
                $rootScope.$broadcast(CONF.EVENT.PANEL_VISIBILITY_CHANGE_EVENT,{ id : id , visible: visible });
                return;
            }

            return panels[id].visible;

        },

        selectPanel: function(arg) {

            var test = function(panel) {
                return arg.id === panel.id;
            };

            if(typeof arg === "string") {
                test = function(panel) {
                    return arg == panel.id;
                }
            }

            angular.forEach(panels, function(value, key) {
                value.active = test(value);
            });

            $rootScope.$broadcast(CONF.EVENT.PANEL_SELECTED_EVENT,{ id : arg });
        }

    }

});