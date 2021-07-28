package com.learn.e;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CourseRepositoryTests {
	
	@Autowired
	private CourseRepository repo;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	public void testCreateCourse() {
		Course course = new Course();
		course.setCourse_name("Java");
		course.setCourse_resource("Udemy");
		course.setCourse_fee("6000");
		course.setCourse_desc("Good Course must take it!");
		
		Course savedCourse = repo.save(course);
		
		Course existCourse = entityManager.find(Course.class, savedCourse.getCourse_id());
		assertThat(existCourse.getCourse_id()).isEqualTo(course.getCourse_id());
		
	}
	

}
