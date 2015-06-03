var TH = angular.module('trainingHelper');

TH.controller('DocumentController', function ($scope, TrainingService) {

    $scope.models = {
        selected: null,
        lists: {"I tuoi allenamenti": [], "Allenamenti da includere nel documento": []}
    };

    TrainingService.list({page : 0 , size : -1},

        function(elements) {
            $scope.models.lists.A = elements.result;
        },

        function(error) {
            alert("Errore caricamento allenamenti.");
        });


});
