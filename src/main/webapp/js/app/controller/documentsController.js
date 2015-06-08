var TH = angular.module('trainingHelper');

TH.controller('DocumentController', function ($scope, TrainingClient, MessagesService) {

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
