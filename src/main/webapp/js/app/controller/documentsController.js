var TH = angular.module('trainingHelper');

TH.controller('DocumentController', function ($scope, TrainingClient, MessagesService) {

    $scope.models = {
        selected: null,
        lists: {
            0 : {
                id: 0,
                label:"I tuoi allenamenti",
                filter:true,
                q: '',
                model:  []
            },

            1 : {
                id: 1,
                label:"Allenamenti da includere nel documento",
                filter:false,
                q: '',
                model: []
            }
        }
    };

    var getItemIndex = function(item, list) {
        for(var i = 0 ; i< list.model.length; i++) {
            if(list.model[i].id == item.id)
                return i;
        }
        return -1;
    };

    var move = function (list, old_index, new_index) {
        list.splice(new_index, 0, list.splice(old_index, 1)[0]);
    };

    $scope.moveToOtherList = function(item,list) {
        var c = 0;
        var toList = list.id == 0 ? $scope.models.lists[1] : $scope.models.lists[0];

        var indexOf = getItemIndex(item, list);
        if(indexOf == -1)
            return;

        list.model.splice(indexOf,1);
        toList.model.splice(toList.model.length, 0, item);
    };

    $scope.showUp = function(item,list) {

        if(list.model.length == 1)
            return false;

        var indexOf = getItemIndex(item, list);
        return indexOf != -1 && indexOf > 0;
    };

    $scope.showDown = function(item,list) {

        if(list.model.length == 1)
            return false;

        var indexOf = getItemIndex(item, list);
        return indexOf != -1 && indexOf != list.model.length - 1;
    };

    $scope.moveUp = function(item,list) {
        var indexOf = getItemIndex(item, list);
        move(list.model, indexOf, indexOf -1);
    };

    $scope.moveDown = function(item,list) {
        var indexOf = getItemIndex(item, list);
        move(list.model, indexOf, indexOf + 1);
    };

    $scope.generateDoc = function() {

        var ids = [];
        $.each($scope.models.lists[1].model, function(key, val) {
           ids.push(val.id);
        });

        $http.post("/rest/training/document", {trainingIds: ids},

            function (data, status, headers) {
                var filename,
                    octetStreamMime = "application/octet-stream",
                    contentType;

                // Get the headers
                headers = headers();

                if (!filename) {
                    filename = headers["x-filename"] || 'invoice.pdf';
                }

                // Determine the content type from the header or default to "application/octet-stream"
                contentType = headers["content-type"] || octetStreamMime;

                if (navigator.msSaveBlob) {
                    var blob = new Blob([data], { type: contentType });
                    navigator.msSaveBlob(blob, filename);
                } else {
                    var urlCreator = window.URL || window.webkitURL || window.mozURL || window.msURL;

                    if (urlCreator) {
                        // Try to use a download link
                        var link = document.createElement("a");

                        if ("download" in link) {
                            // Prepare a blob URL
                            var blob = new Blob([data], { type: contentType });
                            var url = urlCreator.createObjectURL(blob);

                            link.setAttribute("href", url);
                            link.setAttribute("download", filename);

                            // Simulate clicking the download link
                            var event = document.createEvent('MouseEvents');
                            event.initMouseEvent('click', true, true, window, 1, 0, 0, 0, 0, false, false, false, false, 0, null);
                            link.dispatchEvent(event);
                        } else {
                            // Prepare a blob URL
                            // Use application/octet-stream when using window.location to force download
                            var blob = new Blob([data], { type: octetStreamMime });
                            var url = urlCreator.createObjectURL(blob);
                            $window.location = url;
                        }
                    }
                }
            }, function (response) {
                $log.debug(response);
            });
    };

    TrainingClient.list({page : 0 , size : -1},

        function(elements) {
            $scope.models.lists[0].model = elements.result;
        },

        function(error) {
            MessagesService.error("Errore caricamento allenamenti.");
        });


});
