create database ScoreView collate utf8_bin;;

use ScoreView;

create table student (
	student_id char(5) primary key,
	password char(10),
	name nvarchar(50) collate utf8_bin,
	email varchar(30)
);

create table otp (
	otp_code char(5) primary key,
	status int,
	constraint chk_status check(status = 1 or status = 0)
);

create table student_otp_verify (
	otp_code char(5) primary key,
	student_id char(5) unique,
	constraint fk_stv_otp foreign key (otp_code) references otp(otp_code),
	constraint fk_stv_student foreign key (student_id) references student(student_id)
);

create table subject (
	subj_id char(6) primary key,
	subj_name nvarchar(60)
);

create table student_subject (
	student_id char(5),
	subj_id char(6),
	primary key (student_id, subj_id),
	constraint fk_ss_student foreign key (student_id) references student(student_id),
	constraint fk_ss_subject foreign key (subj_id) references subject(subj_id)
);

create table result (
	subj_id char(6) primary key,
	score int,
	submit_date datetime,
	constraint chk_score check(score >= 0  and score <= 10),
	constraint fk_result_subject foreign key (subj_id) references subject(subj_id)
);

create table DPK (
	dpk_id char(5) primary key,
	student_id char(5),
	subj_id char(6),
	constraint fk_dpk_student foreign key (student_id) references student(student_id),
	constraint fk_dpk_subject foreign key (subj_id) references subject(subj_id)
);