package com.imaginea.resumereader.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.imaginea.resumereader.exceptions.ErrorCode;
import com.imaginea.resumereader.exceptions.MyPropertyFieldException;

public class ResumeServiceTest {
	private ResumeService resumeService;
	private String RESUME_DIR = this.getClass().getResource("/testResumes")
			.getPath();
	private String INDEX_DIR = this.getClass().getResource("/testIndex")
			.getPath();

	@Before
	public void setUp() throws IOException {
		resumeService = new ResumeService();
		resumeService.setIndexDirPath("");
		resumeService.setResumeDirPath("");
	}

	@Test
	public void testUpdate1() {
		try {
			resumeService.updateIndex();
		} catch (MyPropertyFieldException mpe) {
			assertEquals(ErrorCode.INDEX_DIR_EMPTY, mpe.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testUpdate2() {
		try {
			resumeService.setIndexDirPath(INDEX_DIR);
			resumeService.updateIndex();
		} catch (MyPropertyFieldException mpe) {
			assertEquals(ErrorCode.RESUME_DIR_EMPTY, mpe.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// valid data
	@Test
	public void testUpdate3() {
		try {
			resumeService.setIndexDirPath(INDEX_DIR);
			resumeService.setResumeDirPath(RESUME_DIR);
			resumeService.updateIndex();
		} catch (MyPropertyFieldException mpe) {
			fail("update test failed");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testSearch1() {
		try {
			resumeService.search("name");
		} catch (MyPropertyFieldException mpe) {
			assertEquals(ErrorCode.INDEX_DIR_EMPTY, mpe.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// valid data
	@Test
	public void testSearch2() {
		try {
			resumeService.setIndexDirPath(INDEX_DIR);
			resumeService.search("name");
		} catch (MyPropertyFieldException mpe) {
			fail("search test failed");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
