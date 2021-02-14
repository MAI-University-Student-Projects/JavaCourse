CREATE TABLE ABONENTS(
    student_id int not null,
    student_name varchar(255),
    primary key (student_id)
);

CREATE TABLE BOOKS(
    book_id int not null,
    book_title varchar(255),
    student_id int,
    foreign key(student_id) references ABONENTS(student_id),
    primary key (book_id)
);