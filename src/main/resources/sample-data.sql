DELETE FROM task_tag WHERE true;
DELETE FROM tag WHERE true;
DELETE FROM task WHERE true;
DELETE FROM project WHERE true;
DELETE FROM note WHERE true;

INSERT INTO note (id, text) VALUES (1, '# Lorem ipsum

Lorem ipsum dolor sit amet, [consectetur adipiscing elit](http://www.example.com).
Pellentesque a commodo nulla, feugiat mattis tortor.

## Duis et eleifend dui.

Nunc id enim quis eros fermentum fermentum. Sed aliquam sodales tellus sed
ultricies. In hac habitasse platea dictumst. Cras scelerisque sagittis tellus,
nec hendrerit metus imperdiet nec. Vivamus quis neque ut tortor hendrerit
tempor quis ac velit. Nullam ut lobortis nisi, sagittis mollis magna. Praesent
risus nisi, auctor id semper nec, sagittis ac nisi. In non ipsum ut elit
faucibus pellentesque sit amet in ex. Aliquam suscipit placerat neque sed
porta.

* Nulla eget sapien at justo pretium lobortis tincidunt quis sapien.
* Mauris quis nisi in dolor porta volutpat.
* Morbi ut mauris commodo, elementum lectus pharetra, malesuada sapien.
* Ut quis urna mattis, placerat felis vitae, efficitur ipsum.
* Aliquam rhoncus mauris eu eros pellentesque, vel elementum velit iaculis.
* Maecenas varius erat nec justo varius, a viverra nisi venenatis.

### Nulla erat odio

Pellentesque vel ullamcorper lorem, ut convallis elit. Class aptent
taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos.

> Proin quam odio, pulvinar vitae quam ac, cursus accumsan nibh. Aliquam justo
> erat, pharetra id sapien vitae, blandit ultricies augue. Nulla non venenatis
> tellus, vitae mollis lectus. Etiam mattis euismod volutpat. Aenean elit est,
> aliquam at rhoncus quis, molestie vel ex. Pellentesque velit massa, auctor
> quis massa et, facilisis blandit lacus. Vivamus gravida, nisi id auctor
> convallis, tellus neque fringilla mauris, nec consequat nunc dui id nisi.

#### Pellentesque interdum

##### Vestibulum

###### Cras tempor nunc
');
INSERT INTO project (id, name, description, created, read_me_id) VALUES (1, 'My first project', 'This is my first sample project.', '2016-07-10T00:29:08+00', 1);
INSERT INTO task (id, summary, description, priority, state, created, updated, project_id) VALUES (1, 'First sample task', '# Lorem ipsum

Lorem ipsum dolor sit amet, [consectetur adipiscing elit](http://www.example.com).
Pellentesque a commodo nulla, feugiat mattis tortor.

## Duis et eleifend dui.

Nunc id enim quis eros fermentum fermentum. Sed aliquam sodales tellus sed
ultricies. In hac habitasse platea dictumst. Cras scelerisque sagittis tellus,
nec hendrerit metus imperdiet nec. Vivamus quis neque ut tortor hendrerit
tempor quis ac velit. Nullam ut lobortis nisi, sagittis mollis magna. Praesent
risus nisi, auctor id semper nec, sagittis ac nisi. In non ipsum ut elit
faucibus pellentesque sit amet in ex. Aliquam suscipit placerat neque sed
porta.

* Nulla eget sapien at justo pretium lobortis tincidunt quis sapien.
* Mauris quis nisi in dolor porta volutpat.
* Morbi ut mauris commodo, elementum lectus pharetra, malesuada sapien.
* Ut quis urna mattis, placerat felis vitae, efficitur ipsum.
* Aliquam rhoncus mauris eu eros pellentesque, vel elementum velit iaculis.
* Maecenas varius erat nec justo varius, a viverra nisi venenatis.

### Nulla erat odio

Pellentesque vel ullamcorper lorem, ut convallis elit. Class aptent
taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos.

> Proin quam odio, pulvinar vitae quam ac, cursus accumsan nibh. Aliquam justo
> erat, pharetra id sapien vitae, blandit ultricies augue. Nulla non venenatis
> tellus, vitae mollis lectus. Etiam mattis euismod volutpat. Aenean elit est,
> aliquam at rhoncus quis, molestie vel ex. Pellentesque velit massa, auctor
> quis massa et, facilisis blandit lacus. Vivamus gravida, nisi id auctor
> convallis, tellus neque fringilla mauris, nec consequat nunc dui id nisi.

#### Pellentesque interdum

##### Vestibulum

###### Cras tempor nunc
', 'HIGH', 'TO_DO', '2016-07-10T00:29:08+00', '2016-07-10T00:29:08+00', 1);
INSERT INTO task (id, summary, description, priority, state, created, updated, project_id) VALUES (2, 'Second sample task', 'This is the second sample task.', 'CRITICAL', 'DONE', '2016-07-10T00:29:08+00', '2016-07-10T00:29:08+00', 1);
INSERT INTO task (id, summary, description, priority, state, created, updated, project_id) VALUES (3, 'Third sample task', 'This is the third sample task.', 'LOW', 'TO_DO', '2016-07-10T00:29:08+00', '2016-07-10T00:29:08+00', 1);
INSERT INTO tag (id, name) VALUES (1, 'Bug');
INSERT INTO tag (id, name) VALUES (2, 'Version 1');
INSERT INTO tag (id, name) VALUES (3, 'Feature');
INSERT INTO task_tag (task_id, tag_id) VALUES (1, 1);
INSERT INTO task_tag (task_id, tag_id) VALUES (1, 2);
INSERT INTO task_tag (task_id, tag_id) VALUES (2, 3);

INSERT INTO project (id, name, description, created) VALUES (2, 'My second project', 'This is my second sample project. Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.', '2016-07-10T00:29:08+00');
INSERT INTO task (id, summary, description, priority, state, created, updated, project_id) VALUES (4, 'Fourth sample task', 'This is the fourth sample task.', 'NORMAL', 'IN_PROGRESS', '2016-07-10T00:29:08+00', '2016-07-10T00:29:08+00', 1);
INSERT INTO task (id, summary, description, priority, state, created, updated, project_id) VALUES (5, 'Fifth sample task', 'This is the fifth sample task.', 'HIGH', 'ON_HOLD', '2016-07-10T00:29:08+00', '2016-07-10T00:29:08+00', 1);

INSERT INTO project (id, name, description, created) VALUES (3, 'My third project', 'This is my third sample project. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.', '2016-07-10T00:29:08+00');
