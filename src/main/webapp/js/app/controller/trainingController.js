var TH = angular.module('trainingHelper');

TH.controller('TrainingController', function ($scope, $q, CONF, $location, $rootScope, TrainingClient,
                                              fileUploader, MessagesService, $routeParams) {

    var upload = function(data) {

        if (angular.version.major <= 1 && angular.version.minor < 2 ) {
            //older versions of angular's q-service don't have a notify callback
            //pass the onProgress callback into the service
            fileUploader
                .post($scope.files, data, function(complete) { $scope.progress(complete); })
                .to($scope.serviceUploadURL)
                .then(function(ret) {
                    scope.done(ret.files, ret.data);
                }, function(error) {
                    scope.error($scope.files,'UPLOAD_ERROR',error);
                })
        } else {
            fileUploader
                .post($scope.files, data)
                .to($scope.serviceUploadURL)
                .then(function(ret) {
                    $scope.done(ret.files, ret.data);
                }, function(error) {
                    $scope.error( $scope.files, 'UPLOAD_ERROR', error);
                },  function(progress) {
                    $scope.progress(progress);
                });
        }
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

    var initTraining = function() {
        $scope.training = {nome:'', descrizione:'', data:new Date().getTime(), images: [] };
    };
    $scope.serviceUploadURL = "/rest/training";
    $scope.training = {};
    initTraining();

    if($routeParams.trainingId !== undefined) {
        $scope.training = $scope.getTrainingById($routeParams.trainingId);
    }

    $scope.onSelection = function(files) {

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

    $scope.progress = function(percentDone) {
        console.log("progress: " + percentDone + "%");
    };

    $scope.done = function(files, data) {
        console.log("upload complete");
        console.log("data: " + JSON.stringify(data));
        writeFiles(files);
    };

    $scope.getData = function(files) {
        //this data will be sent to the server with the files
        return {msg: "from the client", date: new Date()};
    };

    $scope.error = function(files, type, msg) {
        console.log("Upload error: " + msg);
        console.log("Error type:" + type);
        writeFiles(files);
    };

    function writeFiles(files)
    {
        console.log('Files')
        for (var i = 0; i < files.length; i++) {
            console.log('\t' + files[i].name);
        }
    }

});