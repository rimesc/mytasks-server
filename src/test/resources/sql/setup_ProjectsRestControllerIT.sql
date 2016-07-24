INSERT INTO note (id, text) VALUES (1, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.');
INSERT INTO project (id, name, description, created, read_me_id) VALUES (1, 'First project', 'This is my first project.', '2016-07-10T00:29:08+00', 1);
INSERT INTO task (id, summary, description, priority, state, created, updated, project_id) VALUES (1, 'First task', 'My first task', 'HIGH', 'TO_DO', '2016-07-10T00:29:08+00', '2016-07-10T00:29:08+00', 1);
INSERT INTO task (id, summary, description, priority, state, created, updated, project_id) VALUES (2, 'Second task', 'My second task', 'CRITICAL', 'DONE', '2016-07-10T00:29:08+00', '2016-07-10T00:29:08+00', 1);
INSERT INTO task (id, summary, description, priority, state, created, updated, project_id) VALUES (3, 'Third task', 'My third task', 'LOW', 'TO_DO', '2016-07-10T00:29:08+00', '2016-07-10T00:29:08+00', 1);

INSERT INTO note (id, text) VALUES (2, 'Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.');
INSERT INTO project (id, name, description, created, read_me_id) VALUES (2, 'Second project', 'This is my second project.', '2016-07-10T00:29:08+00', 2);
INSERT INTO task (id, summary, description, priority, state, created, updated, project_id) VALUES (4, 'Fourth task', 'My fourth task', 'NORMAL', 'IN_PROGRESS', '2016-07-10T00:29:08+00', '2016-07-10T00:29:08+00', 2);
INSERT INTO task (id, summary, description, priority, state, created, updated, project_id) VALUES (5, 'Fifth task', 'My fifth task', 'HIGH', 'ON_HOLD', '2016-07-10T00:29:08+00', '2016-07-10T00:29:08+00', 2);

INSERT INTO project (id, name, description, created) VALUES (3, 'Third project', 'This is my third project.', '2016-07-10T00:29:08+00');
