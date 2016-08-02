var path = require('path'),
    gulp = require('gulp');

var paths = {
    baseUrl: 'file:' + process.cwd() + '/src/',
    config: ['src/config.js'],
    jspmLibs: ['src/lib/**', '!src/lib/*/test/*'],
    css: {
        files: ['src/css/*.css']
    },
    images: ["src/images/*"],
    partials: ["src/partials/*"],
    modals: ["src/modals/*"],
    components: ["src/components/*"],
    destination: './dist'
};

gulp.task('copy-css', function() {
    return gulp.src(paths.css.files)
        .pipe(gulp.dest(paths.destination + '/css'));
});

gulp.task('copy-js', function() {
    return gulp.src('src/js/*.js')
        .pipe(gulp.dest(paths.destination + '/js'));
});

// Copy jspm-managed JavaScript dependencies to "dist" folder
gulp.task('copy-lib', function() {
    return gulp.src(paths.jspmLibs)
        .pipe(gulp.dest(paths.destination + '/lib'));
});

gulp.task('copy-config', function() {
    return gulp.src(paths.config)
        .pipe(gulp.dest(paths.destination));
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

gulp.task('build', ['copy-css', 'copy-js', 'copy-lib',
    'copy-config', 'copy-images', 'copy-partials', 'copy-modals', 'copy-components'], function(){});

