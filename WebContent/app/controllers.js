app.controller('HomeController', ['$scope', '$translate', 'availableLanguages', '$window',  function ($scope, $translate, availableLanguages, $window) {
    $scope.language = {
        available: availableLanguages,
        select: function (language) {
            $window.localStorage.defaultLanguage = language;
            $translate.use(language);
        },
        selected: $translate.use()
    };
        
    
}]);

app.controller('LoginController', ['$scope', '$location', 'handshake', 'AuthFactory', '$window', function ($scope, $location, handshake, AuthFactory, $window) {

    var helpers = {
        initIfValidHandshake: function (initCallback) {
            if (typeof handshake === 'object' && handshake.hasOwnProperty('data') && handshake.data.hasOwnProperty('isLoggedIn') && handshake.data.isLoggedIn === true) {
                this.redirectToAdmin();
            } else if (typeof initCallback == 'function') {
                initCallback();
            }
        },
        setFeedbackMessage: function (status, message) {
            if (status == 'danger') {
                $window.console.error(message);
            }
            $scope.auth.feedback = {status: status, message: message};
        },
        processLoginFeedback: function (feedbackData) {
            if (typeof feedbackData == 'object' && feedbackData.hasOwnProperty('status')) {
                this.setFeedbackMessage(feedbackData.status, feedbackData.message);
                if (feedbackData.status == 'success') {
                    /**
                     * Commented out, because /rest/handshake.json has hardcoded isLoggenIn. Just set it to false and refresh the page to auto-redirec.
                     * uncoment next line if REST supports auth.
                     */
//                    this.redirectToAdmin();
                }
            } else {
                this.setFeedbackMessage('danger', 'Unknown server error. Check console for details');
            }
        },
        redirectToAdmin: function () {
            $location.url('/admin');
        }
    };


    (function () {

        var init = function () {
            $scope.auth = {
                feedback: null,
                credentials: AuthFactory.reset(),
                login: function () {
                    this.feedback = null;
                    var authRestCall = AuthFactory.login(this.credentials);
                    authRestCall.success(function (data) {
                        helpers.processLoginFeedback(data);
                    });
                }
            };
        };

        helpers.initIfValidHandshake(init);

    }());

}]);

app.controller('AdminController', ['$scope', '$window', '$location', 'RestService', 'handshake', function ($scope, $window, $location, RestService, handshake) {

    var helpers = {
        initIfValidHandshake: function (initCallback) {
            if (typeof handshake !== 'object' || !handshake.hasOwnProperty('data') || !handshake.data.hasOwnProperty('isLoggedIn') || handshake.data.isLoggedIn !== true) {
                this.redirectToLogin();
            } else if (typeof initCallback == 'function') {
                initCallback();
            }
        },
        redirectToLogin: function () {
            $location.url('/login');
        }
    };

    var data = {
        load: function () {
            var restCall = RestService.get('secure/data.json');
            restCall.success(function (externalData) {
                $scope.data = externalData.list;
            })
        }
    };

    (function () {

        var init = function () {
            $scope.user = handshake.data.user;
            $scope.data = null;
            data.load();
        };

        helpers.initIfValidHandshake(init);

    }());

}]);

app.controller('StartController', ['$scope', '$window', 'RestService', function ($scope, $window, RestService) {

    var CONST = {
        some_error_occured: 'Some error occured'
    };

    $scope.myMood = JSON.parse($window.localStorage.myMood);

    $scope.myRating = {
        selected: null
    }

    $scope.help = {
        isVisible: false,
        messages: {
            info: 'Information or errors will appear here',
            text: 'This is the text you should evaluate',
            rating: 'This is the different ratings you can give the text',
            more: 'If you can not decide which sentimental value the text contains, then you can ask for more. You should not use this if you think the text is neutral',
            skip: 'If you want to skip this text',
            done: 'When you have given the text a sentimental value that you think is correct, you can continue here',
            mood: 'Your current mood level. You should change this if it has changed'
        }
    };

    $scope.moreText = function () {
        console.log("Fetching more text");

        $scope.myRating.selected = null;
    }

    $scope.skipRating = function () {
        console.log("Skipped rating");

        $scope.myRating.selected = null;
    }

    $scope.commitRating = function () {
        console.log("Commited rating " + $scope.myRating.selected + " with mood " + $scope.myMood.id);

        $scope.myRating.selected = null;
    }

    var restCall = RestService.get('text.json');
    restCall.success(function (data) {
        $scope.text = data.text;
    });
    restCall.error(function () {
        $scope.text = CONST.some_error_occured;
    });

    var restCall = RestService.get('ratings.json');
    restCall.success(function (data) {
        $scope.ratings = data.ratings;
    });
    restCall.error(function () {
        $scope.text = CONST.some_error_occured;
    });
}]);


app.controller('MoodController', ['$scope', '$window', 'RestService', function ($scope, $window, RestService) {

    var CONST = {
        some_error_occured: 'Some error occured'
    };

    $scope.myMood = {
        selected: null
    }

    $scope.help = {
        isVisible: false,
        messages: {
            actors: 'help text here! asdljnas dkasd asdlasd  asld asd help text here! asdljnas dkasd asdlasd  asld asd '
        }
    };

    $scope.setMood = function (mood) {
        $window.localStorage.myMood = JSON.stringify(mood);
        $scope.myMood.selected = mood.id;
    }

    var restCall = RestService.get('moods.json');
    restCall.success(function (data) {
        $scope.moods = data.moods;
    });
    restCall.error(function () {
        $scope.text = CONST.some_error_occured;
    });
}]);

