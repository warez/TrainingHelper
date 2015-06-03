var TH = angular.module('trainingHelper');

TH.controller('TrainingController', function ($scope, CONF, $location, $rootScope, $routeParams) {

    var initTraining = function() {
        $scope.training = {nome:'', descrizione:'', data:new Date().getTime(), images: [] };
    };

    $scope.training = {};
    initTraining();

    if($routeParams.trainingId !== undefined) {
        $scope.training = $scope.getTrainingById($routeParams.trainingId);
    }

    $rootScope.$on(CONF.EVENT.TRAINING_DELETE, function(args) {

        if($scope.training.id == args.trainingId) {
            $rootScope.$broadcast(CONF.EVENT.SELECT_PANEL_EVENT, { panelId : "home" });
            $location.path("/");
            initTraining();
        }

    });

});