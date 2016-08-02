var path = require('path'),
    gulp = require('gulp'),
    less = require('gulp-less'),
    cssMinify = require('gulp-minify-css');

var paths = {
    baseUrl: 'file:' + process.cwd() + '/src/',
    libs: ['bower_components/**'],
    css: [],
    less: ['src/less/*'],
    fonts: ['bower_components/bootstrap/fonts/*'],
    images: ["src/images/*"],
    partials: ["src/partials/*"],
    modals: ["src/modals/*"],
    components: ["src/components/*"],
    destination: './dist'
};

gulp.task('copy-css', function() {
    return gulp.src(paths.css)
        .pipe(gulp.dest(paths.destination + '/css'));
});

gulp.task('copy-js', function() {
    return gulp.src('src/js/*.js')
        .pipe(gulp.dest(paths.destination + '/js'));
});

gulp.task('copy-lib', function() {
    return gulp.src(paths.libs)
        .pipe(gulp.dest(paths.destination + '/lib'));
});

gulp.task('copy-images', function() {
    return gulp.src(paths.images)
        .pipe(gulp.dest(paths.destination + '/images'));
});

gulp.task('copy-partials', function() {
    return gulp.src(paths.partials)
        .pipe(gulp.dest(paths.destination + '/partials'))
});

gulp.task('copy-modals', function() {
    return gulp.src(paths.modals)
        .pipe(gulp.dest(paths.destination + '/modals'))
});

gulp.task('copy-components', function() {
    return gulp.src(paths.components)
        .pipe(gulp.dest(paths.destination + '/components'))
});

gulp.task('less', function () {
    return gulp.src(paths.less)
        .pipe(less())
        .pipe(cssMinify({noRebase: true}))
        .pipe(gulp.dest(paths.destination + '/css'));
});

// copies fonts from bootstrap to where they can be referenced by the compiled CSS
gulp.task('copy-fonts', function() {
    return gulp.src(paths.fonts)
        .pipe(gulp.dest(paths.destination + '/fonts'));
});

gulp.task('default', ['copy-css', 'copy-js', 'copy-lib',
    'copy-images', 'copy-partials', 'copy-modals', 'copy-components', 'less', 'copy-fonts'], function(){});

