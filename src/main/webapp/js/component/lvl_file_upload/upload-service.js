var module;

try {
    module = angular.module('lvl.services');
} catch (e) {
    module  = angular.module('lvl.services', []);
}

module.factory('fileUploader', ['$rootScope', '$http', function($rootScope, $http) {
    var svc = {
        post: function(files, obj) {

            return {
                to: function(uploadUrl)
                {
                    return $http({
                        method: 'POST',
                        url: uploadUrl,
                        //IMPORTANT!!! You might think this should be set to 'multipart/form-data'
                        // but this is not true because when we are sending up files the request
                        // needs to include a 'boundary' parameter which identifies the boundary
                        // name between parts in this multi-part request and setting the Content-type
                        // manually will not set this boundary parameter. For whatever reason,
                        // setting the Content-type to 'false' will force the request to automatically
                        // populate the headers properly including the boundary parameter.
                        headers: { 'Content-Type': undefined },
                        //This method will allow us to change how the data is sent up to the server
                        // for which we'll need to encapsulate the model data in 'FormData'
                        transformRequest: function (data) {
                            var formData = new FormData();
                            //need to convert our json object to a string version of json otherwise
                            // the browser will do a 'toString()' on the object which will result
                            // in the value '[Object object]' on the server.
                            formData.append("data", angular.toJson(data.model));
                            //now add all of the assigned files
                            for (var i = 0; i < files.length; i++) {
                                //add each file to the form data and iteratively name them
                                formData.append("file", files[i]);
                            }
                            return formData;
                        },
                        //Create an object that contains the model and files which will be transformed
                        // in the above transformRequest method
                        data: { model: obj, files: files }
                    });
                }
            };
        }
    };

    return svc;
}]);