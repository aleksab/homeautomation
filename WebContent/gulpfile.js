var gulp = require('gulp'),
  connect = require('gulp-connect');

gulp.task('connect', function() {
  connect.server({
    root: '',
    livereload: true
  });
});

gulp.task('html', function () {
  gulp.src(['./app/**/*', './index.html'])
    .pipe(connect.reload());
});

gulp.task('watch', function () {
  gulp.watch(['./app/**/*', './index.html'], ['html']);
});

gulp.task('default', ['connect', 'watch']);
