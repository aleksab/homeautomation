var JSCollections = {
    plugins: [
        'bower_components/angular/angular.min.js',
        'bower_components/angular-route/angular-route.min.js'
    ],
    app: [
        'app/*.js'
    ],
    scripts: []
};

module.exports = function (config) {
    config.set({
        basePath: '',
        frameworks: ['jasmine', 'ng-scenario'],
        files: []
            .concat(JSCollections.plugins)
            .concat(JSCollections.app)
            .concat(JSCollections.scripts)
            .concat(['bower_components/angular-mocks/angular-mocks.js', 'test/*Test.js']),
        exclude: ['app/config.js'],
        reporters: ['dots'],

        port: 8080,
        runnerPort: 9100,

        colors: true,

        // possible values: LOG_DISABLE || LOG_ERROR || LOG_WARN || LOG_INFO || LOG_DEBUG
        logLevel: config.LOG_INFO,

        // enable / disable watching file and executing tests whenever any file changes
        autoWatch: false,

        // Start these browsers, currently available:
        // - Chrome
        // - ChromeCanary
        // - Firefox
        // - Opera
        // - Safari (only Mac)
        // - PhantomJS
        // - IE (only Windows)
        browsers: [
            'PhantomJS'
//            ,'Chrome'
        ],

        // If browser does not capture in given timeout [ms], kill it
        captureTimeout: 10000,

        // Continuous Integration mode
        // if true, it capture browsers, run tests and exit
        singleRun: true
    });
};