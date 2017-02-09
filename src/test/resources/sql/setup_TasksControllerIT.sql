INSERT INTO project (id, name, description, owner, created) VALUES (1, 'First project', 'My first project.', 'test_user', '2016-07-10T00:29:08+00');
INSERT INTO task (id, summary, description, priority, state, created, updated, project_id) VALUES (1, 'First task', 'My first task', 'NORMAL', 'TO_DO', '2016-07-10T00:29:08+00', '2016-07-10T00:29:08+00', 1);
INSERT INTO tag (id, name) VALUES (1, 'First');
INSERT INTO tag (id, name) VALUES (2, 'Second');
INSERT INTO task_tag (task_id, tag_id) VALUES (1, 1);
INSERT INTO task_tag (task_id, tag_id) VALUES (1, 2);

INSERT INTO project (id, name, description, owner, created) VALUES (2, 'Second project', 'My second project.', 'test_user', '2016-07-10T00:29:08+00');

INSERT INTO task (id, summary, description, priority, state, created, updated, project_id) VALUES (2, 'Second task', 'My second task', 'LOW', 'TO_DO', '2016-07-10T00:29:08+00', '2016-07-10T00:29:08+00', 2);
INSERT INTO task (id, summary, description, priority, state, created, updated, project_id) VALUES (3, 'Third task', 'My third task', 'NORMAL', 'IN_PROGRESS', '2016-07-10T00:29:08+00', '2016-07-10T00:29:08+00', 2);
INSERT INTO task (id, summary, description, priority, state, created, updated, project_id) VALUES (4, 'Fourth task', 'My fourth task', 'HIGH', 'ON_HOLD', '2016-07-10T00:29:08+00', '2016-07-10T00:29:08+00', 2);
INSERT INTO task (id, summary, description, priority, state, created, updated, project_id) VALUES (5, 'Fifth task', 'My fifth task', 'CRITICAL', 'DONE', '2016-07-10T00:29:08+00', '2016-07-10T00:29:08+00', 2);

INSERT INTO project (id, name, description, owner, created) VALUES (3, 'Third project', 'My third project.', 'test_user', '2016-07-10T00:29:08+00');
INSERT INTO task (id, summary, description, priority, state, created, updated, project_id) VALUES (6, 'Sixth task', 'My sixth task', 'NORMAL', 'TO_DO', '2016-07-10T00:29:08+00', '2016-07-10T00:29:08+00', 3);
INSERT INTO task (id, summary, description, priority, state, created, updated, project_id) VALUES (7, 'Seventh task', 'My seventh task', 'NORMAL', 'DONE', '2016-07-10T00:29:08+00', '2016-07-10T00:29:08+00', 3);
