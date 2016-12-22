package dk.magenta.bitmagasinet;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import dk.magenta.bitmagasinet.checksum.FileChecksum;
import dk.magenta.bitmagasinet.checksum.FileChecksumImpl;
import dk.magenta.bitmagasinet.remote.BitrepositoryConnector;
import dk.magenta.bitmagasinet.remote.BitrepositoryConnectorStub;
import dk.magenta.bitmagasinet.remote.ThreadStatus;

public class TestControllerImpl {

	private ControllerImpl controller;
	private List<FileChecksum> fileChecksums;
	private FileChecksum fileChecksum1;
	private FileChecksum fileChecksum2;
	private FileChecksum fileChecksum3;
	private BitrepositoryConnector bitrepositoryConnector;

	@Before
	public void setUp() {
		controller = new ControllerImpl();
		fileChecksums = new ArrayList<FileChecksum>();
		addFileChecksum1();
	}

	@Test
	public void shouldReturnSizeZeroWhenRemainingFileChecksumListEmpty() {
		assertEquals(0, controller.getRemainingFileChecksums().size());
	}

	@Test
	public void shouldReturnSizeOneWhenRemainingFileChecksumListHasOneElement() {
		controller.addFileChecksumsToRemainingList(fileChecksums);
		assertEquals(1, controller.getRemainingFileChecksums().size());
		assertEquals("file1.bin", controller.getRemainingFileChecksums().get(0).getFilename());
	}

	@Test
	public void shouldReturnSizeTwoWhenRemainingFileChecksumListHasTwoElement() {
		addFileChecksum2();
		controller.addFileChecksumsToRemainingList(fileChecksums);
		assertEquals(2, controller.getRemainingFileChecksums().size());
		assertEquals("file1.bin", controller.getRemainingFileChecksums().get(0).getFilename());
		assertEquals("file2.bin", controller.getRemainingFileChecksums().get(1).getFilename());
	}
	
	@Test
	public void shouldReturnSize0ForProcessedFileChecksumsWhenNoneHaveBeenProcessed() {
		assertEquals(0, controller.getProcessedFileChecksums().size());
	}
	
	@Test
	public void shouldReturnSize1ForProcessedFileChecksumsWhenOneHaveBeenProcessed() throws InterruptedException {
		addFileChecksum2();
		controller.addFileChecksumsToRemainingList(fileChecksums);
		
		// Two FileChecksums are now in the remaining list
		
		processFileChecksum(fileChecksum1, ThreadStatus.SUCCESS);
		
		assertEquals(1, controller.getProcessedFileChecksums().size());
		assertEquals("file1.bin", controller.getProcessedFileChecksums().get(0).getFilename());
	}
	
	@Test
	public void whenFirstFileChecksumIsProcessedItShouldBeRemovedFromTheRemainingList() throws InterruptedException {
		addFileChecksum2();
		controller.addFileChecksumsToRemainingList(fileChecksums);
		
		// Two FileChecksums are now in the remaining list

		processFileChecksum(fileChecksum1, ThreadStatus.SUCCESS);

		assertEquals(1, controller.getRemainingFileChecksums().size());
		assertEquals("file2.bin", controller.getRemainingFileChecksums().get(0).getFilename());

	}

	@Test
	public void shouldProcess3FileChecksumsCorrectly() throws InterruptedException {
		addFileChecksum2();
		addFileChecksum3();
		controller.addFileChecksumsToRemainingList(fileChecksums);
		
		// Three FileChecksums are now in the remaining list
		
		assertEquals(3, controller.getRemainingFileChecksums().size());
		assertEquals("file1.bin", controller.getRemainingFileChecksums().get(0).getFilename());
		assertEquals("file2.bin", controller.getRemainingFileChecksums().get(1).getFilename());
		assertEquals("file3.bin", controller.getRemainingFileChecksums().get(2).getFilename());

		processFileChecksum(fileChecksum1, ThreadStatus.SUCCESS);
		
		assertEquals(2, controller.getRemainingFileChecksums().size());
		assertEquals("file2.bin", controller.getRemainingFileChecksums().get(0).getFilename());
		assertEquals("file3.bin", controller.getRemainingFileChecksums().get(1).getFilename());
		assertEquals(1, controller.getProcessedFileChecksums().size());
		assertEquals("file1.bin", controller.getProcessedFileChecksums().get(0).getFilename());

		processFileChecksum(fileChecksum2, ThreadStatus.SUCCESS);

		assertEquals(1, controller.getRemainingFileChecksums().size());
		assertEquals("file3.bin", controller.getRemainingFileChecksums().get(0).getFilename());
		assertEquals(2, controller.getProcessedFileChecksums().size());
		assertEquals("file1.bin", controller.getProcessedFileChecksums().get(0).getFilename());
		assertEquals("file2.bin", controller.getProcessedFileChecksums().get(1).getFilename());

		processFileChecksum(fileChecksum3, ThreadStatus.SUCCESS);
		
		assertEquals(0, controller.getRemainingFileChecksums().size());
		assertEquals(3, controller.getProcessedFileChecksums().size());
		assertEquals("file1.bin", controller.getProcessedFileChecksums().get(0).getFilename());
		assertEquals("file2.bin", controller.getProcessedFileChecksums().get(1).getFilename());
		assertEquals("file3.bin", controller.getProcessedFileChecksums().get(2).getFilename());

	}

	@Test
	public void shouldPutFileChecksumBackInLineInCaseOfError() {
		addFileChecksum2();
		addFileChecksum3();
		controller.addFileChecksumsToRemainingList(fileChecksums);
		
		processFileChecksum(fileChecksum1, ThreadStatus.ERROR);
		
		assertEquals(3, controller.getRemainingFileChecksums().size());
		assertEquals("file2.bin", controller.getRemainingFileChecksums().get(0).getFilename());
		assertEquals("file3.bin", controller.getRemainingFileChecksums().get(1).getFilename());
		assertEquals("file1.bin", controller.getRemainingFileChecksums().get(2).getFilename());
		assertEquals(0, controller.getProcessedFileChecksums().size());
		
		
	}
	
	@Ignore
	@Test
	public void shouldReturnProgressHandler() {
		assertNotNull(controller.getProgressHandler());
	}
	
	private void addFileChecksum1() {
		fileChecksum1 = new FileChecksumImpl("file1.bin", "9e5aae9572765f8bec9bca8c818188da", "64");
		fileChecksum1.setRemoteChecksum("9e5aae9572765f8bec9bca8c818188da");
		fileChecksums.add(fileChecksum1);
	}
	
	private void addFileChecksum2() {
		fileChecksum2 = new FileChecksumImpl("file2.bin", "9e5aae9572765f8bec9bca8c818188da", "64");
		fileChecksum2.setRemoteChecksum("00ffae9572765f8bec9bca8c81812211");
		fileChecksums.add(fileChecksum2);
	}

	private void addFileChecksum3() {
		fileChecksum3 = new FileChecksumImpl("file3.bin", "9e5aae9572765f8bec9bca8c818188da", "64");
		fileChecksum3.setRemoteChecksum("9e5aae9572765f8bec9bca8c818188da");
		fileChecksums.add(fileChecksum3);
	}
	
	private void processFileChecksum(FileChecksum fileChecksum, ThreadStatus status) {
		bitrepositoryConnector = new BitrepositoryConnectorStub(fileChecksum, status);
		bitrepositoryConnector.addObserver(controller);

		controller.processNext(bitrepositoryConnector);
		try {
			Thread.sleep(200); // To make sure that the separate thread will finish
		} catch (InterruptedException e) {}

	}

	// should update progress handler after one file
	// should update progress handler after two file
	// show hold BitrepositoProgressHandler
	// handle messagebus error

}