INSERT INTO project (id, name, description, owner, created) VALUES (1, 'First project', 'My first project.', 'test_user', '2016-07-10T00:29:08+00');
INSERT INTO task (id, summary, description, priority, state, created, updated, project_id) VALUES (1, 'First task', 'My first task', 'NORMAL', 'TO_DO', '2016-07-10T00:29:08+00', '2016-07-10T00:29:08+00', 1);
INSERT INTO tag (id, name) VALUES (1, 'First');
INSERT INTO tag (id, name) VALUES (2, 'Second');
INSERT INTO task_tag (task_id, tag_id) VALUES (1, 1);
INSERT INTO task_tag (task_id, tag_id) VALUES (1, 2);
INSERT INTO task (id, summary, description, priority, state, created, updated, project_id) VALUES (2, 'Second task', 'My second task', 'LOW', 'TO_DO', '2016-07-10T00:29:08+00', '2016-07-10T00:29:08+00', 1);
INSERT INTO tag (id, name) VALUES (3, 'Third');
INSERT INTO task_tag (task_id, tag_id) VALUES (2, 3);
