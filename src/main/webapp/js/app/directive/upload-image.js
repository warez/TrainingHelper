var MODULE = angular.module("fileupload", ['angularUUID2']);

MODULE.directive('fileSelector', ['uuid2', '$q', function(uuid2, $q) {
        return {

            restrict: 'E',
            replace: true,

            scope: {
                chooseFileButtonText: '@',
                maxFiles: '@',
                maxFileSizeKb: '@',
                onSelection: '&',
                onError: '&'
            },

            template: '<div>' +
            '<input type="file" style="opacity:0" />' +
            '<label class="lvl-choose-button" ng-click="choose()">{{chooseFileButtonText}}</label>' +
            '</div>',

            compile: function compile(tElement, tAttrs, transclude) {
                var fileInput = angular.element(tElement.children()[0]);
                var fileLabel = angular.element(tElement.children()[1]);

                if (!tAttrs.maxFiles) {
                    tAttrs.maxFiles = 1;
                    fileInput.removeAttr("multiple")
                } else {
                    fileInput.attr("multiple", "multiple");
                }

                if (!tAttrs.maxFileSizeKb) {
                    tAttrs.maxFileSizeKb = 200;
                }

                var fileId = uuid2.newuuid();
                fileInput.attr("id", fileId);
                fileLabel.attr("for", fileId);

                return function postLink(scope, el, attrs, ctl) {

                    var fileRead  = function(file) {
                        var deferred = $q.defer();

                        var reader = new FileReader();

                        reader.onload = function() {
                            deferred.resolve(reader.result);
                        };

                        reader.readAsDataURL(file);

                        return deferred.promise;
                    };

                    var loadFileFunc = function(result) {
                        return result;
                    };

                    scope.files = [];

                    scope.callOnSelection = function() {

                        var thenResult = [];

                        for(var i = 0; i< scope.files.length; i++) {
                            var c = fileRead(scope.files[i]).then(loadFileFunc);
                            thenResult.push(c);
                        }

                        scope.fromThen = $q.all(thenResult)
                            .then(function(values) {
                                scope.onSelection({files: values});
                            });
                    };

                    el.bind('change', function(e) {
                        if (!e.target.files.length) return;

                        scope.files = [];
                        var tooBig = [];
                        if (e.target.files.length > scope.maxFiles) {
                            raiseError(e.target.files, 'TOO_MANY_FILES', "Cannot upload " + e.target.files.length + " files, maxium allowed is " + scope.maxFiles);
                            return;
                        }

                        for (var i = 0; i < scope.maxFiles; i++) {
                            if (i >= e.target.files.length) break;

                            var file = e.target.files[i];
                            scope.files.push(file);

                            if (file.size > scope.maxFileSizeKb * 1024) {
                                tooBig.push(file);
                            }
                        }

                        if (tooBig.length > 0) {
                            raiseError(tooBig, 'MAX_SIZE_EXCEEDED', "Files are larger than the specified max (" + scope.maxFileSizeMb + "MB)");
                            return;
                        }

                        scope.$apply(function(){scope.callOnSelection()})

                    });

                    function raiseError(files, type, msg) {
                        scope.onError({files: files, type: type, msg: msg});
                        resetFileInput();
                    }

                    function resetFileInput() {
                        var parent = fileInput.parent();

                        fileInput.remove();
                        var input = document.createElement("input");
                        var attr = document.createAttribute("type");
                        attr.nodeValue = "file";
                        input.setAttributeNode(attr);

                        var inputId = uuid2.newuuid();
                        attr = document.createAttribute("id");
                        attr.nodeValue = inputId;
                        input.setAttributeNode(attr);

                        attr = document.createAttribute("style");
                        attr.nodeValue = "opacity: 0;display:inline;width:0";
                        input.setAttributeNode(attr);

                        if (scope.maxFiles > 1) {
                            attr = document.createAttribute("multiple");
                            attr.nodeValue = "multiple";
                            input.setAttributeNode(attr);
                        }

                        fileLabel.after(input);
                        fileLabel.attr("for", inputId);

                        fileInput = angular.element(input);
                    }
                }
            }
        }
    }]);