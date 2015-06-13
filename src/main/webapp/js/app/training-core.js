var TH = angular.module('trainingHelper', ["ngRoute", "trainingHelperConf",
    "trainingHelperClient", "trainingHelperService", "angular-spinkit",
    "textAngular", "angularUtils.directives.dirPagination",
    "dndLists", "cgBusy",
    "lvl.directives.fileupload",
    "lvl.services"]);

TH.controller('MainController', function ($scope, CONF, $rootScope, $location, TrainingClient, MessagesService) {

    $scope.messages = MessagesService.getMessages();
    $scope.allTrainings = [];
    $scope.trainingCount=-1;
    $scope.currentPage = 1;
    $scope.PAGE_SIZE = CONF.PAGE_SIZE;

    $scope.CONF = CONF;

    $rootScope.$on(CONF.EVENT.TRAINING_CHANGED, function(args) {
        $scope.loadTrainings(1);
    });

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
        if(!confirm("Eliminare l'allenamento: '" + training.nome +"'?"))
            return;

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

    $scope.loadTrainings = function(page) {

        $scope.trainingOperation = TrainingClient.list({page : page - 1 , size : $scope.PAGE_SIZE},

            function(elements) {
                $scope.allTrainings = elements.result;
                $scope.trainingCount = elements.count;
                $scope.currentPage = elements.page + 1;

                $rootScope.$broadcast(CONF.EVENT.TRAINING_LOADED, { loaded : $scope.allTrainings, trainingCount : $scope.trainingCount } );
                $rootScope.$broadcast(CONF.EVENT.SELECT_PANEL_EVENT, { panelId: "home"} );
                $location.path("/");
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