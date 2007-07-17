INSERT INTO resource(resource_id) VALUES(1)
INSERT INTO project_category_lu(project_category_id) VALUES(1)
INSERT INTO project(project_id, project_category_id) VALUES(1, 1)
INSERT INTO upload(upload_id, project_id, resource_id) VALUES(1, 1, 1)
INSERT INTO upload(upload_id, project_id, resource_id) VALUES(2, 1, 1)
INSERT INTO upload(upload_id, project_id, resource_id) VALUES(3, 1, 1)
INSERT INTO screening_status_lu(screening_status_id, name, description, create_user, create_date, modify_user, modify_date) VALUES(1, 'Pending', 'Pending', 'gavin', CURRENT, 'gavin', CURRENT)
INSERT INTO screening_task(screening_task_id, upload_id, screening_status_id, screener_id, start_timestamp, create_user, create_date, modify_user, modify_date) VALUES(1, 1, 1, 1, CURRENT, 'gavin', CURRENT, 'gavin', CURRENT)
INSERT INTO response_severity_lu(response_severity_id, name, description, create_user, create_date, modify_user, modify_date) VALUES(1, 'Warning', 'Warning!', 'gavin', CURRENT, 'gavin', CURRENT)
INSERT INTO screening_response_lu(screening_response_id, response_severity_id, response_code, response_text, create_user, create_date, modify_user, modify_date) VALUES(1, 1, 1, 'Strongly Agree', 'gavin', CURRENT, 'gavin', CURRENT)
INSERT INTO screening_result(screening_result_id, screening_task_id, screening_response_id, dynamic_response_text, create_user, create_date, modify_user, modify_date) VALUES(1, 1, 1, 'it is ok.', 'gavin', CURRENT, 'gavin', CURRENT)
