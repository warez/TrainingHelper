var TH = angular.module('trainingHelper');

TH.controller('LoginController', function ($scope, $rootScope, $timeout, LoginClient, LoginService, CONF) {

    $scope.logged = false;

    $scope.label = "Login";
    $scope.op = "login";
    $scope.message = "";

    LoginService.checkLogin();
    $timeout( LoginService.checkLogin, 10000);

    $rootScope.$on(CONF.EVENT.LOGIN_STATUS_CHANGE, function(event, status) {

        $scope.label = status.logged ? "Logout" : "Login";
        $scope.op = status.logged ? "logout" : "login";
        $scope.message = status.logged ? "Bentornato " + status.nickName + "!" : "";

    });

});

TH.controller('LoginRedirectController', function ($scope, CONF, $location, $rootScope, $routeParams) {

    $window.location.href = "/rest/user/" + $routeParams.op;

});