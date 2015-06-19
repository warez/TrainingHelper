var TH = angular.module('trainingHelper', ["ngRoute", "trainingHelperConf",
    "trainingHelperClient", "trainingHelperService", "angular-spinkit",
    "textAngular", "angularUtils.directives.dirPagination",
    "dndLists", "cgBusy","fileupload","ui.bootstrap"]);

TH.controller('MainController', function ($scope, CONF, $rootScope, $location, TrainingClient, MessagesService, $modal) {

    $scope.messages = MessagesService.getMessages();
    $scope.allTrainings = [];
    $scope.trainingCount=-1;
    $scope.currentPage = 1;
    $scope.PAGE_SIZE = CONF.PAGE_SIZE;

    $scope.CONF = CONF;

    $rootScope.$on(CONF.EVENT.TRAINING_CHANGED, function(event, args) {
        $scope.loadTrainings(1);
    });

    $scope.showConfirmDialog = function(title, message, size) {

        var modalInstance = $modal.open({
            animation: true,
            templateUrl: 'confirmTemplate.html',
            controller: 'ConfirmDialogController',
            size: size || 'sm',
            resolve: {
                data: function () {
                    return {
                        title: title,
                        message: message
                    };
                }
            }
        });

        return modalInstance.result;

    };

    $scope.showInputDialog = function(title, message, value) {
        var modalInstance = $modal.open({
            animation: true,
            templateUrl: 'inputTemplate.html',
            controller: 'InputDialogController',
            size: 'sm',
            resolve: {
                data: function () {
                    return {
                        title: title,
                        message: message,
                        value: value
                    };
                }
            }
        });

        return modalInstance.result;
    };


    $scope.getTrainingById = function(id) {
        for(var i = 0 ; i< $scope.allTrainings.length; i++) {
            if($scope.allTrainings[i].id == id)
                return $scope.allTrainings[i];
        }
        return undefined;
    };

    $scope.editTraining = function(trainingId) {
        $location.path("/edit/" + trainingId);
        $rootScope.$broadcast(CONF.EVENT.SELECT_PANEL_EVENT, { panelId: "edit"} );
    };

    $scope.onTrainingDelete = function(training) {

        var onOk = function() {
            $scope.trainingOperation = TrainingClient.delete({trainingId: training.id}, training,

                function(training) {
                    $rootScope.$broadcast(CONF.EVENT.TRAINING_DELETE, { trainingId: training.id } );
                    $scope.loadTrainings(1);
                },

                function(training) {
                    MessagesService.error("Allenamento '" + training.nome + "' non cancellato.");
                }
            );
        };

        $scope.showConfirmDialog("Attenzione", "Eliminare l'allenamento: '" + training.nome +"'?").then(function(result){
            if(result)
                onOk();
        });

    };

    $scope.loadTrainings = function(page) {

        $scope.trainingListOperation = TrainingClient.list({page : page - 1 , size : $scope.PAGE_SIZE},

            function(elements) {
                $scope.allTrainings = elements.result;
                $scope.trainingCount = elements.count;
                $scope.currentPage = elements.page + 1;

                $rootScope.$broadcast(CONF.EVENT.TRAINING_LOADED, { loaded : $scope.allTrainings, trainingCount : $scope.trainingCount } );

            },

            function(error) {
                MessagesService.error("Errore caricamento allenamenti.");
            });
    };

    $rootScope.$on(CONF.EVENT.LOGIN_STATUS_CHANGE, function(event, status) {

        $scope.logged = status.logged;

        if(status.logged)
            $scope.loadTrainings(1);
    });

});

TH.controller('InputDialogController',function($scope, $modalInstance, data){
    $scope.message = data.message;
    $scope.title = data.title;
    $scope.value = data.value;

    $scope.ok = function () {
        $modalInstance.close($scope.value);
    };

    $scope.cancel = function () {
        $modalInstance.dismiss("cancel");
    };

}).controller('ConfirmDialogController',function($scope, $modalInstance, data){
    $scope.message = data.message;
    $scope.title = data.title;

    $scope.ok = function () {
        $modalInstance.close(true);
    };

    $scope.cancel = function () {
        $modalInstance.close(false);
    };
});