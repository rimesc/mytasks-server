var gulp = require('gulp'),
    less = require('gulp-less'),
    cssMinify = require('gulp-minify-css');

var bower = 'bower_components',
    resources = 'src/main/resources/static',
    target = 'target/classes/static';

gulp.task('less', function () {
    return gulp.src([resources + '/less/**'])
        .pipe(less())
        .pipe(cssMinify({noRebase: true}))
        .pipe(gulp.dest(target + '/css'));
});

gulp.task('copy-lib', function() {
	return gulp.src([bower + '/**']).pipe(gulp.dest(target + '/lib'));
});

// copies fonts from bootstrap to where they can be referenced by the compiled CSS
gulp.task('copy-fonts', function() {
    return gulp.src([bower + '/bootstrap/fonts/**']).pipe(gulp.dest(target + '/fonts'));
});

gulp.task('default', ['less', 'copy-lib', 'copy-fonts'], function(){});

