DELETE FROM categories;

INSERT INTO categories (id, name, description, is_deleted) VALUE
    (1, 'Test name1', 'Description test1', false),
    (2, 'Test name2', 'Description test2', false),
    (3, 'Test name3', 'Description test3', false);
