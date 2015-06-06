var TH = angular.module('trainingHelper');

TH.controller('DocumentController', function ($scope, TrainingClient, CONF) {

    $scope.models = {
        selected: null,
        lists: {
            0 : {
                label:"I tuoi allenamenti",
                filter:true,
                q: '',
                model:  []
            },

            1 : {
                label:"Allenamenti da includere nel documento",
                filter:false,
                q: '',
                model: []
            }
        }
    };

    TrainingClient.list({page : 0 , size : -1},

        function(elements) {
            $scope.models.lists[0].model = elements.result;
        },

        function(error) {
            alert("Errore caricamento allenamenti.");
        });


});
