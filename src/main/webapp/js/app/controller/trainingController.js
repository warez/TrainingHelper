var TH = angular.module('trainingHelper');

TH.controller('TrainingController', function ($scope, $q, CONF, $location, $rootScope,
                                              TrainingClient, MessagesService, $routeParams) {

    $scope.maxFileReachedMessage = "Numero massimo file consentito raggiunto.";
    $scope.serviceUploadURL = "/rest/training";
    $scope.training = {};

    var initTraining = function() {

        if($routeParams.trainingId !== undefined) {
            $scope.training = $scope.getTrainingById($routeParams.trainingId);
        }else {
            $scope.training = {nome: '', descrizione: '', data: new Date().getTime(), images: []};
        }

    };

    $scope.showFileSelector = function() {
        return $scope.training.images.length < CONF.MAX_FILES_PER_TRAINING;
    };

    var saveTraining = function(training) {
        $scope.trainingOperation = TrainingClient.save({}, training,

            function (data) {
                $rootScope.$broadcast(CONF.EVENT.TRAINING_CHANGED, {training:data});
                MessagesService.info("Allenamento salvato correttamente");
            },

            function (error) {
                MessagesService.error("Errore durante il savataggio dell'allenamento.");
            }
        );
    };

    initTraining();

    $scope.onSelection = function(files) {

        var totalCount = $scope.training.images.length + files.length;
        if(totalCount > CONF.MAX_FILES_PER_TRAINING) {
            MessagesService.error("Numero file massimo consentito: " + CONF.MAX_FILES_PER_TRAINING);
            return;
        }

        for(var i = 0; i < files.length; i++) {
            var image = { image : files[i]};
            $scope.training.images.push(image);
        }

    };

    $scope.removeImage = function(index) {
        $scope.training.images.splice(index,1);
    };

    $scope.onTrainingChange = function(training) {
        saveTraining( training );
    };

    $scope.onTrainingSave = function(training) {
        if(training.id)
            remove(training.id);

        saveTraining( training );
    };

    $rootScope.$on(CONF.EVENT.TRAINING_DELETE, function(args) {

        if($scope.training.id == args.trainingId) {
            $rootScope.$broadcast(CONF.EVENT.SELECT_PANEL_EVENT, { panelId : "home" });
            $location.path("/");
            initTraining();
        }

    });

});