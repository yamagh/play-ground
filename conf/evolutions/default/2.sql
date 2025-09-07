# --- Sample dataset

# --- !Ups

insert into app_user
  (email,password,name,created_on,updated_on,deleted_on,created_by,updated_by,deleted_by)
values
  ('admin@invalid.com','FFFFFF','Admin',now(),null,null,'system',null,null)
, ('user1@invalid.com','FFFFFF','User1',now(),null,null,'system',null,null)
, ('user2@invalid.com','FFFFFF','User2',now(),null,null,'system',null,null)
;

insert into task
  (title,description,status,owner_id,due_date,priority,parent_task_id,created_on,updated_on,deleted_on,created_by,updated_by,deleted_by)
values
  ('Project Alpha Kick-off', 'Plan and hold the kick-off meeting for Project Alpha. Prepare slides and agenda.', 'Done', 1, '2024-05-10 17:00:00', 1, null, '2024-05-01 09:00:00', '2024-05-10 16:30:00', null, 'system', 'system', null)
, ('Requirement Gathering for Feature X', 'Interview stakeholders to gather requirements for the new Feature X.', 'WIP', 2, '2024-06-25 18:00:00', 1, 1, '2024-05-11 10:00:00', '2024-06-10 11:00:00', null, 'system', 'system', null)
, ('UI/UX Design for Mobile App', 'Create wireframes and high-fidelity mockups for the new mobile application.', 'WIP', 3, '2024-07-05 18:00:00', 1, null, '2024-05-15 14:00:00', '2024-06-11 09:30:00', null, 'system', 'system', null)
, ('Develop Authentication Module', 'Implement user registration, login, and password reset functionality.', 'Open', 1, '2024-07-15 18:00:00', 1, 3, '2024-06-01 11:00:00', null, null, 'system', null, null)
, ('API Endpoint for User Profile', 'Create GET, POST, PUT endpoints for user profile management.', 'Open', 2, '2024-07-20 18:00:00', 2, 4, '2024-06-01 11:30:00', null, null, 'system', null, null)
, ('Frontend for Dashboard', 'Develop the main dashboard view using React and Redux.', 'WIP', 3, '2024-07-10 18:00:00', 1, 3, '2024-06-05 10:00:00', '2024-06-12 15:00:00', null, 'system', 'system', null)
, ('Unit Testing for Payment Gateway', 'Write comprehensive unit tests for the payment processing logic.', 'Open', 1, '2024-07-18 18:00:00', 2, 4, '2024-06-10 16:00:00', null, null, 'system', null, null)
, ('Fix Bug #404 - Page Not Found', 'Investigate and fix the 404 error on the /profile page for logged-in users.', 'Done', 2, '2024-06-05 12:00:00', 1, null, '2024-06-03 10:00:00', '2024-06-04 17:00:00', null, 'system', 'system', null)
, ('Deploy to Staging Environment', 'Deploy the latest build from the develop branch to the staging server.', 'Done', 3, '2024-06-07 15:00:00', 1, null, '2024-06-07 10:00:00', '2024-06-07 14:00:00', null, 'system', 'system', null)
, ('User Acceptance Testing (UAT)', 'Coordinate with the QA team and stakeholders to perform UAT.', 'WIP', 1, '2024-06-28 18:00:00', 2, 9, '2024-06-08 11:00:00', '2024-06-11 16:00:00', null, 'system', 'system', null)
, ('Prepare Release Notes', 'Write release notes for version 2.1.0.', 'Open', 2, '2024-07-01 18:00:00', 3, 10, '2024-06-12 10:00:00', null, null, 'system', null, null)
, ('Marketing Campaign for New Feature', 'Plan and execute a marketing campaign for the upcoming Feature X.', 'Open', 3, '2024-08-01 18:00:00', 2, null, '2024-06-12 11:00:00', null, null, 'system', null, null)
, ('Update User Documentation', 'Update the online help documents to reflect the new features in v2.1.0.', 'WIP', 1, '2024-07-12 18:00:00', 3, 10, '2024-06-12 12:00:00', '2024-06-13 10:00:00', null, 'system', 'system', null)
, ('Setup CI/CD Pipeline', 'Configure Jenkins pipeline for automated build, test, and deployment.', 'Done', 2, '2024-05-20 18:00:00', 1, null, '2024-05-01 10:00:00', '2024-05-20 17:00:00', null, 'system', 'system', null)
, ('Database Schema Migration', 'Write and test the SQL migration script for the new comments feature.', 'WIP', 3, '2024-06-29 18:00:00', 1, 22, '2024-06-13 09:00:00', '2024-06-13 14:00:00', null, 'system', 'system', null)
, ('Performance Tuning for Search API', 'Analyze and optimize the performance of the product search API.', 'Open', 1, '2024-07-25 18:00:00', 2, null, '2024-06-13 10:00:00', null, null, 'system', null, null)
, ('Code Review for PR #55', 'Review the pull request for the new caching mechanism.', 'Done', 2, '2024-06-12 18:00:00', 2, null, '2024-06-11 15:00:00', '2024-06-12 11:00:00', null, 'system', 'system', null)
, ('Refactor Legacy Codebase', 'Refactor the old user service to use modern design patterns.', 'Cancel', 3, '2024-09-01 18:00:00', 3, null, '2024-05-10 10:00:00', '2024-06-10 10:00:00', null, 'system', 'system', null)
, ('Create E2E tests for registration', 'Use Cypress to create end-to-end tests for the user registration flow.', 'WIP', 1, '2024-07-08 18:00:00', 2, 4, '2024-06-13 11:00:00', '2024-06-14 10:00:00', null, 'system', 'system', null)
, ('Design DB schema for comments', 'Design the database tables required for the comments feature.', 'Done', 2, '2024-06-10 18:00:00', 1, 22, '2024-06-05 10:00:00', '2024-06-10 17:00:00', null, 'system', 'system', null)
, ('Implement real-time notifications', 'Use WebSockets to provide real-time notifications to users.', 'Open', 3, '2024-08-15 18:00:00', 2, null, '2024-06-14 09:00:00', null, null, 'system', null, null)
, ('Write spec for Project Beta', 'Write the technical specification document for the upcoming Project Beta.', 'Open', 1, '2024-07-01 18:00:00', 1, null, '2024-06-01 10:00:00', null, null, 'system', null, null)
, ('Set up monitoring and alerts', 'Configure Datadog for production monitoring and set up critical alerts.', 'WIP', 2, '2024-06-30 18:00:00', 1, null, '2024-06-10 10:00:00', '2024-06-13 17:00:00', null, 'system', 'system', null)
, ('Onboard new developer', 'Help the new developer (John) get his development environment set up.', 'Done', 3, '2024-06-14 18:00:00', 3, null, '2024-06-13 10:00:00', '2024-06-14 16:00:00', null, 'system', 'system', null)
, ('Investigate customer issue #812', 'A customer is reporting that they cannot update their email address. Investigate.', 'WIP', 1, '2024-06-21 18:00:00', 1, null, '2024-06-14 11:00:00', '2024-06-14 11:30:00', null, 'system', 'system', null)
, ('Create presentation for sprint review', 'Prepare slides for the sprint 5 review meeting.', 'Done', 2, '2024-06-14 12:00:00', 2, null, '2024-06-12 10:00:00', '2024-06-14 11:00:00', null, 'system', 'system', null)
, ('Update third-party libraries', 'Check for security updates for all third-party libraries and update them.', 'Open', 3, '2024-07-01 18:00:00', 3, null, '2024-06-15 10:00:00', null, null, 'system', null, null)
, ('Configure load balancer', 'Configure AWS ELB for the new microservice.', 'Done', 1, '2024-06-01 18:00:00', 2, null, '2024-05-28 10:00:00', '2024-06-01 16:00:00', null, 'system', 'system', null)
, ('Security audit of the application', 'Perform a security audit to identify potential vulnerabilities.', 'Open', 2, '2024-08-10 18:00:00', 1, null, '2024-06-15 11:00:00', null, null, 'system', null, null)
, ('Create backup and recovery plan', 'Document the backup and disaster recovery plan for the main database.', 'WIP', 3, '2024-07-15 18:00:00', 2, null, '2024-06-10 10:00:00', '2024-06-14 14:00:00', null, 'system', 'system', null)
, ('Translate UI to Japanese', 'Work with translators to get all UI strings translated to Japanese.', 'Open', 1, '2024-08-20 18:00:00', 3, null, '2024-06-17 10:00:00', null, null, 'system', null, null)
, ('A/B Testing for landing page', 'Set up an A/B test for the new landing page design to measure conversion.', 'WIP', 2, '2024-07-05 18:00:00', 2, 12, '2024-06-17 11:00:00', '2024-06-18 10:00:00', null, 'system', 'system', null)
, ('Analyze user engagement metrics', 'Analyze metrics from last month to identify user behavior patterns.', 'Done', 3, '2024-06-15 18:00:00', 3, null, '2024-06-10 10:00:00', '2024-06-15 17:00:00', null, 'system', 'system', null)
, ('Write blog post about new release', 'Write a blog post announcing the v2.1.0 release and its new features.', 'Open', 1, '2024-07-03 18:00:00', 3, 12, '2024-06-18 10:00:00', null, null, 'system', null, null)
, ('Prepare Q3 roadmap', 'Plan the features and initiatives for the Q3 product roadmap.', 'WIP', 2, '2024-06-28 18:00:00', 1, null, '2024-06-15 10:00:00', '2024-06-18 11:00:00', null, 'system', 'system', null)
, ('Team building activity planning', 'Organize a team building event for the engineering department.', 'Done', 3, '2024-06-20 18:00:00', 3, null, '2024-06-01 10:00:00', '2024-06-18 15:00:00', null, 'system', 'system', null)
, ('Fix accessibility issues', 'Fix WCAG 2.1 AA compliance issues on the settings page.', 'WIP', 1, '2024-07-11 18:00:00', 2, null, '2024-06-18 13:00:00', '2024-06-19 10:00:00', null, 'system', 'system', null)
, ('Optimize image assets', 'Compress and resize images across the application to improve load times.', 'Done', 2, '2024-06-10 18:00:00', 3, null, '2024-06-05 10:00:00', '2024-06-10 16:00:00', null, 'system', 'system', null)
, ('Set up analytics dashboard', 'Create a Grafana dashboard to visualize key application metrics.', 'WIP', 3, '2024-07-02 18:00:00', 2, 23, '2024-06-19 09:00:00', '2024-06-20 10:00:00', null, 'system', 'system', null)
, ('Research competitor features', 'Analyze features of three main competitors.', 'Cancel', 1, '2024-06-30 18:00:00', 3, null, '2024-06-01 10:00:00', '2024-06-15 10:00:00', null, 'system', 'system', null)
, ('Create user survey', 'Create a survey to gather feedback on user satisfaction.', 'Open', 2, '2024-07-10 18:00:00', 3, 12, '2024-06-20 11:00:00', null, null, 'system', null, null)
, ('POC for using a new framework', 'Build a proof-of-concept for using Vue.js for the new admin panel.', 'WIP', 3, '2024-07-31 18:00:00', 2, null, '2024-06-15 10:00:00', '2024-06-20 14:00:00', null, 'system', 'system', null)
, ('Document internal APIs', 'Use Swagger/OpenAPI to document all internal REST APIs.', 'Open', 1, '2024-08-01 18:00:00', 2, null, '2024-06-20 15:00:00', null, null, 'system', null, null)
, ('Improve test coverage to 80%', 'Increase the unit test coverage of the core module from 65% to 80%.', 'WIP', 2, '2024-07-25 18:00:00', 2, null, '2024-06-18 10:00:00', '2024-06-21 10:00:00', null, 'system', 'system', null)
, ('Set up local dev environment guide', 'Write a guide for new developers on setting up their local environment.', 'Done', 3, '2024-06-18 18:00:00', 3, 24, '2024-06-14 10:00:00', '2024-06-18 17:00:00', null, 'system', 'system', null)
, ('Organize sprint retrospective', 'Organize the retrospective meeting for the current sprint.', 'Done', 1, '2024-06-21 17:00:00', 3, null, '2024-06-20 10:00:00', '2024-06-21 16:00:00', null, 'system', 'system', null)
, ('Plan next sprint''s tasks', 'Groom the backlog and plan tasks for the next sprint.', 'WIP', 2, '2024-06-24 18:00:00', 1, null, '2024-06-21 10:00:00', '2024-06-21 11:00:00', null, 'system', 'system', null)
, ('Resolve merge conflicts in develop', 'Resolve merge conflicts between feature/ABC and develop branch.', 'Done', 3, '2024-06-20 12:00:00', 1, null, '2024-06-20 10:00:00', '2024-06-20 11:30:00', null, 'system', 'system', null)
, ('Production Deployment v2.1.0', 'Execute the production deployment plan for version 2.1.0.', 'Open', 1, '2024-07-05 03:00:00', 1, 10, '2024-06-21 14:00:00', null, null, 'system', null, null)
, ('Post-deployment monitoring', 'Monitor system health and logs after the v2.1.0 deployment.', 'Open', 2, '2024-07-06 12:00:00', 1, 49, '2024-06-21 14:00:00', null, null, 'system', null, null)
;

# --- !Downs

delete from app_user;
delete from task;
